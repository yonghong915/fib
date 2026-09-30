package com.fib;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class ShardDispatcher {
    private static final int MAX_DB_CONCURRENCY = 8;
    private final Semaphore semaphore = new Semaphore(MAX_DB_CONCURRENCY);

    private final Executor executor;
    private final ProgressTracker progress;
    // 【关键】单个子分片最大订单数量，防止超大分片长期占用许可 200
    private static final int SHARD_SUB_CHUNK_SIZE = 15;

    public ShardDispatcher(Executor executor, ProgressTracker progress) {
        this.executor = executor;
        this.progress = progress;
    }

    static void main() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();) {
            ShardDispatcher shardDispatcher = new ShardDispatcher(executor, new ProgressTracker());
            List<SettlementOrder> orders = buildOrderData();
            shardDispatcher.submit("20260927", orders);
        }
    }

    private static List<SettlementOrder> buildOrderData() {
        String[] accts = new String[]{"01020304034", "012030203402", "0203020302"};
        List<SettlementOrder> orders = new ArrayList<>();
        int rows = 100;
        for (int i = 0; i < rows; i++) {
            orders.add(new SettlementOrder(accts[RandomUtil.randomInt(0, accts.length)], 100000L));
        }
        return orders;
    }

    public BatchReport submit(String batchDay, List<SettlementOrder> orders) {
        Map<String, List<SettlementOrder>> shards = orders.stream()
                .collect(Collectors.groupingBy(SettlementOrder::getAcctNo));
        progress.beginBatch(batchDay, orders.size(),
                orders.stream().mapToLong(SettlementOrder::getAmt).sum());

        List<CompletableFuture<ShardResult>> futures = new ArrayList<>();
        for (Map.Entry<String, List<SettlementOrder>> entry : shards.entrySet()) {
            String acctNo = entry.getKey();
            List<SettlementOrder> rawAcctOrders = entry.getValue();
            // 对当前账号订单，切割成多个子chunk
            List<List<SettlementOrder>> subChunks = splitChunk(rawAcctOrders, SHARD_SUB_CHUNK_SIZE);

            // 同账号子分片串行链式执行：前一个完成，才跑下一个
            CompletableFuture<ShardResult> accountChain = CompletableFuture.completedFuture(null);
            for (List<SettlementOrder> chunk : subChunks) {
                accountChain = accountChain.thenCompose(prev ->
                        CompletableFuture.supplyAsync(() -> settleShard(acctNo, chunk), executor)
                                .orTimeout(15, TimeUnit.MINUTES)
                                .exceptionally(ex -> ShardResult.failed(acctNo, ex))
                );
            }
            log.info("acctNo=[{}] finished.", acctNo);
            System.out.println("acctNo:" + acctNo);
            futures.add(accountChain);
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        return progress.finishBatch(futures.stream().map(CompletableFuture::join).toList());
    }

    /**
     * 分片切割工具：把大列表切成多个小chunk
     */
    private List<List<SettlementOrder>> splitChunk(List<SettlementOrder> source, int chunkSize) {
        List<List<SettlementOrder>> chunks = new ArrayList<>();
        for (int i = 0; i < source.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, source.size());
            chunks.add(source.subList(i, end));
        }
        return chunks;
    }

    private ShardResult settleShard(String acctNo, List<SettlementOrder> shard) {
        System.out.println("do finished " + acctNo + " begin");
        boolean acquired = false;
        try {
            // ✅ 信号量闸门：进入DB处理前获取许可，超时防止永久挂虚拟线程
            acquired = semaphore.tryAcquire(12, TimeUnit.MINUTES);
            if (!acquired) {
                throw new RuntimeException("DB信号量获取超时，acctNo=" + acctNo);
            }
            for (SettlementOrder o : shard) {
                try {
                    //settleService.settleOne(o); // CAS + Outbox 短事务
                } catch (DataAccessException e) {
                    // 分片内有限重试
//                    retryTemplate.execute(ctx -> {
//                        settleService.settleOne(o);
//                        return null;
//                    });
                    progress.markFailed(o);
                }
            }
            System.out.println("do finished " + acctNo + " end");
            return ShardResult.ok(acctNo, shard.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("分片任务中断，acctNo=" + acctNo, e);
        } finally {
            // ✅ 只有成功拿到许可才释放，避免release多次
            if (acquired) {
                semaphore.release();
            }
        }
    }
}
