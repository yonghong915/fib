package com.fib.ecny.trans.comp.netty;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.channel.Channel;

import java.util.concurrent.Executors;

public class MsgDisruptorManager {
    // 环形缓冲区大小，2的幂次，根据并发调整 1024/2048/4096
    private static final int RING_BUFFER_SIZE = 1024 * 8;
    private Disruptor<MsgEvent> disruptor;
    private RingBuffer<MsgEvent> ringBuffer;

    // 单例初始化
    public void start() {
        // 事件工厂
        EventFactory<MsgEvent> factory = MsgEvent::newInstance;
        // 单生产者模式（Netty多IO线程并发生产安全）
        disruptor = new Disruptor<>(factory, RING_BUFFER_SIZE,
                Executors.newFixedThreadPool(4),
                ProducerType.MULTI,
                new BlockingWaitStrategy()); // 高并发可选YieldingWaitStrategy

        // 业务消费者，多线程消费
        disruptor.handleEventsWithWorkerPool(new MsgEventHandler(), new MsgEventHandler(), new MsgEventHandler());
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    // Netty IO线程投递消息到Disruptor（无锁入队）
    public void publish(Channel channel, MsgDto msg) {
        long seq = ringBuffer.next();
        try {
            MsgEvent event = ringBuffer.get(seq);
            event.clear();
            event.setChannel(channel);
            event.setMsg(msg);
        } finally {
            ringBuffer.publish(seq);
        }
    }

    public void shutdown() {
        disruptor.shutdown();
    }

    // 业务消费处理器（纯业务逻辑，不阻塞Netty IO线程）
    static class MsgEventHandler implements WorkHandler<MsgEvent> {
        @Override
        public void onEvent(MsgEvent event) throws Exception {
            Channel channel = event.getChannel();
            MsgDto msg = event.getMsg();


            // 执行业务逻辑：DB、缓存、复杂计算
            System.out.println("处理消息：" + msg.getData());
            // 回写响应
            MsgDto resp = handleBusienss(msg);
            channel.writeAndFlush(resp);
        }

        private MsgDto handleBusienss(MsgDto msg) {
            MsgDto resp = new MsgDto();
            return resp;
        }
    }
}
