package com.fib.ecny.base.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean("traceAsyncExecutor")
    public Executor traceAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("trace-async-");
        // 任务装饰器，拷贝ThreadContext
        executor.setTaskDecorator(new TraceTaskDecorator());
        executor.initialize();
        // TTL包装，解决多层异步、CompletableFuture场景
        return TtlExecutors.getTtlExecutorService(executor.getThreadPoolExecutor());
    }

    static class TraceTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> parentCtx = org.apache.logging.log4j.ThreadContext.getImmutableContext();
            return () -> {
                try {
                    ThreadContext.putAll(parentCtx);
                    runnable.run();
                } finally {
                    ThreadContext.clearAll();
                }
            };
        }
    }
}
