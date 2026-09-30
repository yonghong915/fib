package com.fib;

import lombok.Data;

@Data
public class ShardResult {
    public static <U> U failed(String key, Throwable ex) {
        return null;
    }

    public static ShardResult ok(String acctNo, int size) {
        return null;
    }
}
