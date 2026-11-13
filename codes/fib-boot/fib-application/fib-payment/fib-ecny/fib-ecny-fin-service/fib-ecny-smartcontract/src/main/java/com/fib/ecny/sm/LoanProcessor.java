package com.fib.ecny.sm;

import com.fib.ecny.sm.serial.TranSerialIds;

public interface LoanProcessor<B> {
    String tranCode();

    Class<B> bodyType();

    LoanResponse process(LoanRequest request, B body, TranSerialIds ids);
}
