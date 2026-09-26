package com.fib.ecny.jobtask;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.jobtask.service.PayRollDetailService;
import com.fib.ecny.jobtask.service.PayrollService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class BatchTaskService {
    //最大并发数15
    private static final int MAX_CONCURRENT = 15;
    //总超时 11分钟，预留缓冲
    private static final long GLOBAL_TIMEOUT_MIN = 11;
    // 每一批从DB查询多少条
    private static final int PAGE_QUERY_SIZE = 2000;

    //存放处理完成后的结果对象
    private final BlockingQueue<ProcessResultDto>[] writeQueues;

    private static final int BATCH_WRITE_SIZE = 500;
    // 批次大小：一个CompletableFuture处理多少条
    private static final int TASK_BUNDLE_SIZE = 50;
    //DB写入额外预留等待时长
    private static final long WRITE_WAIT_TIMEOUT_MIN = 2;
    // 信号量，精准控制并发数量
    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENT);
    // 统计
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final ConcurrentLinkedQueue<Long> failIdQueue = new ConcurrentLinkedQueue<>();
    private static final int WRITE_THREAD_COUNT = 3;
    private static final int PER_WRITE_QUEUE_CAPACITY = 2000;
    private static final ProcessResultDto WRITE_POISON = new ProcessResultDto();
    //TODO
    private final AtomicInteger queryCount = new AtomicInteger(0);
    private final List<Thread> writeThreadList = new ArrayList<>();
    @Resource
    private PayrollService payrollService;

    @Resource
    private PayRollDetailService payRollDetailService;

    @SuppressWarnings("unchecked")
    public BatchTaskService() {
        writeQueues = new BlockingQueue[WRITE_THREAD_COUNT];
        for (int i = 0; i < WRITE_THREAD_COUNT; i++) {
            writeQueues[i] = new ArrayBlockingQueue<>(PER_WRITE_QUEUE_CAPACITY);
        }
    }

    /**
     * 游标分页查询一批数据: where id > lastId limit pageSize
     *
     * @param lastMaxId 上一页最大id，第一页传0
     * @return 当前页列表
     */
    private List<PayrollDetailDto> queryPageData(Long batchId,Long lastMaxId, int pageSize) {
        return payRollDetailService.queryList(batchId,lastMaxId,pageSize);
    }

    /**
     * 单条业务处理逻辑
     */
    private ProcessResultDto handleSingle(PayrollDetailDto dto) {
        ProcessResultDto resultDto = new ProcessResultDto();
        resultDto.setBatchId(dto.getBatchId());
        resultDto.setSeqId(dto.getSeqId());
        try {
            TimeUnit.MILLISECONDS.sleep(RandomUtil.randomLong(20, 100));
            resultDto.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            log.error("业务处理失败 seqId:{}", dto.getSeqId(), e);
            resultDto.setSuccess(Boolean.FALSE);
        }
        return resultDto;
    }

    /**
     * 批量入库并更新统计（写入线程执行）
     */
    private void batchSaveResult(List<ProcessResultDto> buffer) {
        if (buffer.isEmpty()) {
            return;
        }
        // TODO: 此处填入jdbcTemplate批量update代码
        // jdbcTemplate.batchUpdate(......);
        long successNum = buffer.stream().filter(ProcessResultDto::isSuccess).count();
        successCount.addAndGet((int) successNum);
        buffer.stream()
                .filter(r -> !r.isSuccess())
                .map(ProcessResultDto::getSeqId)
                .forEach(failIdQueue::offer);
    }

    /**
     * 启动多写消费线程
     */
    private void startAllWriteThreads() {
        for (int i = 0; i < WRITE_THREAD_COUNT; i++) {
            final int idx = i;
            Thread writeThread = new Thread(() -> {
                List<ProcessResultDto> buffer = new ArrayList<>(BATCH_WRITE_SIZE);
                try {
                    while (true) {
                        ProcessResultDto result = writeQueues[idx].take();
                        if (result == WRITE_POISON) {
                            if (!buffer.isEmpty()) {
                                batchSaveResult(buffer);
                                buffer.clear();
                            }
                            break;
                        }
                        buffer.add(result);
                        if (buffer.size() >= BATCH_WRITE_SIZE) {
                            batchSaveResult(buffer);
                            buffer.clear();
                        }
                    }
                    log.info("写线程 {} 正常退出", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("写线程 {} 被中断", Thread.currentThread().getName(), e);
                }
            }, "write-thread-" + idx);
            writeThread.start();
            writeThreadList.add(writeThread);
        }
    }

    /**
     * 哈希路由队列，同一个id固定一个写线程，避免锁冲突，修复负数下标越界
     */
    private BlockingQueue<ProcessResultDto> selectQueue(Long dataId) {
        int slot = (int) ((dataId % WRITE_THREAD_COUNT) + WRITE_THREAD_COUNT) % WRITE_THREAD_COUNT;
        return writeQueues[slot];
    }

    /**
     * 主入口：执行十万条全量批量任务
     */
    public void executeBatchTask() {
        long startTime = System.currentTimeMillis();
        long lastQueryMaxId = 0L;
        startAllWriteThreads();
        Executor executor = Executors.newVirtualThreadPerTaskExecutor();
        Long batchId = 0L;
        try {
            while (true) {
                // 1.游标分页拉取一页数据
                List<PayrollDetailDto> pageDataList = queryPageData(batchId,lastQueryMaxId, PAGE_QUERY_SIZE);
                if (pageDataList.isEmpty()) {
                    // 没有更多数据，读取阶段结束
                    break;
                }
                // 更新游标id，下一页从更大id开始查
                lastQueryMaxId = pageDataList.get(pageDataList.size() - 1).getSeqId();
                // 将本页数据切分成多个小包，每包 TASK_BUNDLE_SIZE 条
                List<List<PayrollDetailDto>> bundleList = ListUtil.partition(pageDataList, TASK_BUNDLE_SIZE);
                for (List<PayrollDetailDto> bundle : bundleList) {
                    // 信号量控制并发
                    semaphore.acquire();
                    CompletableFuture.runAsync(() -> {
                        try {
                            //当前批次内逐条计算
                            for (PayrollDetailDto source : bundle) {
                                ProcessResultDto result = handleSingle(source);
                                try {
                                    selectQueue(source.getSeqId()).put(result);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    log.error("任务被中断，bundle部分数据丢失 seqId:{}", source.getSeqId(), e);
                                    failIdQueue.offer(source.getSeqId());
                                } catch (Exception ex) {
                                    log.error("bundle任务未知异常 seqId:{}", source.getSeqId(), ex);
                                    failIdQueue.offer(source.getSeqId());
                                }
                            }
                        } finally {
                            semaphore.release();
                        }
                    }, executor).whenComplete((unused, ex) -> {
                        if (ex != null) {
                            log.error("异步任务执行异常", ex);
                        }
                    });
                    // 【高性能改造关键点】不再add future，不再执行removeIf扫描
                    printProgress(startTime);
                }
            }

            // =========等待所有生产者异步任务全部完成=========
            waitAllProducerTaskDone();
            //waitAllWriteThreadFinish();
            // =========【重要】所有业务任务结束，投递毒丸消息关闭写线程=========
            sendPoisonToAllWriteQueue();

            // 额外预留时间等待写线程完成入库
            waitAllWriteThreadFinish(WRITE_WAIT_TIMEOUT_MIN, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("批量任务主线程被中断", e);
        } catch (Exception e) {
            log.error("批量任务主循环异常", e);
        } finally {
            shutdownGracefully();
            // =====打印精确总耗时=====
            long totalTimeMs = System.currentTimeMillis() - startTime;
            long totalSec = totalTimeMs / 1000;
            log.info("【任务总耗时】总毫秒数：{} ms, 总秒数：{} s", totalTimeMs, totalSec);
            printFinalResult(startTime);
        }
    }

    /**
     * 等待所有生产者异步任务执行完毕，基于信号量等待
     */
    private void waitAllProducerTaskDone() throws InterruptedException {
        long deadline = System.currentTimeMillis() + GLOBAL_TIMEOUT_MIN * 60 * 1000;
        while (semaphore.availablePermits() < MAX_CONCURRENT) {
            long remain = deadline - System.currentTimeMillis();
            if (remain <= 0) {
                throw new InterruptedException("生产者任务等待超时");
            }
            Thread.sleep(200);
        }
        log.info("所有生产者处理任务全部完成");
    }

    /**
     * 向所有写队列发送毒丸消息（内部捕获中断异常，保证全部发送）
     */
    private void sendPoisonToAllWriteQueue() {
        for (BlockingQueue<ProcessResultDto> queue : writeQueues) {
            try {
                queue.put(WRITE_POISON);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("发送毒丸消息被中断", e);
                continue;
            }
        }
    }

    /**
     * 等待所有写线程执行完毕
     */
    private void waitAllWriteThreadFinish(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        for (Thread t : writeThreadList) {
            long remain = deadline - System.currentTimeMillis();
            if (remain <= 0) {
                log.warn("等待写线程超时，强制结束");
                break;
            }
            t.join(remain);
        }
    }

    /**
     * 优雅关闭所有资源
     */
    private void shutdownGracefully() {
        // 关闭worker线程池
//        WORK_POOL.shutdown();
//        try {
//            if (!WORK_POOL.awaitTermination(5, TimeUnit.MINUTES)) {
//                WORK_POOL.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            WORK_POOL.shutdownNow();
//        }
        //兜底唤醒阻塞写线程
        for (Thread t : writeThreadList) {
            if (t.isAlive()) {
                t.interrupt();
            }
        }
    }

    private void printProgress(long startTime) {
        long costSec = (System.currentTimeMillis() - startTime) / 1000;
        costSec = Math.max(costSec, 1);
        long done = successCount.get() + failIdQueue.size();
        long tps = done / costSec;
        //打印队列积压
        StringBuilder queueSizeInfo = new StringBuilder();
        for (int i = 0; i < writeQueues.length; i++) {
            queueSizeInfo.append("Q").append(i).append(":").append(writeQueues[i].size()).append(" ");
        }
        log.info("进度：已处理={},失败={},耗时秒={},TPS={},目标≥167",
                done, failIdQueue.size(), costSec, tps);
    }

    private void printFinalResult(long startTime) {
        long total = successCount.get() + failIdQueue.size();
        log.info("====任务结束====");
        log.info("入库成功：{}", successCount.get());
        log.info("失败：{}", failIdQueue.size());
        log.info("处理总条数：{}", total);
        log.info("失败id列表：{}", failIdQueue);
    }

    public static void main(String[] args) {
        BatchTaskService batchTaskService = new BatchTaskService();
        batchTaskService.executeBatchTask();
    }
}