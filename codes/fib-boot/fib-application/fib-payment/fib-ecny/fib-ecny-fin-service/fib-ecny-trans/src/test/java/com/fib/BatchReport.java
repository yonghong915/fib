package com.fib;

import java.util.List;

public class BatchReport {
    public static BatchReport of(List<ShardResult> list) {
        return new BatchReport();
    }
}
