package com.fib;

import java.util.List;

public class ProgressTracker {
    public void beginBatch(String batchDay, int size, long sum) {
    }

    public BatchReport finishBatch(List<ShardResult> list) {
        System.out.println("处理完成");
        return  null;
    }

    public void markFailed(SettlementOrder o) {
    }
}
