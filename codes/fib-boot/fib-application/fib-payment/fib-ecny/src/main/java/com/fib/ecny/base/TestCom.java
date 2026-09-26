package com.fib.ecny.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCom {
    static void main() {
        List<CompletableFuture<BizContext>> futures = new ArrayList<>();
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                CompletableFuture<BizContext> future = CompletableFuture.supplyAsync(() -> {
                    BizContext bizContext = new BizContext();
                    return bizContext;
                }, executorService);
                futures.add(future);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        List<BizContext> resultList = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        long sum = resultList.stream().mapToLong(BizContext::getDuration).sum();
    }
}
