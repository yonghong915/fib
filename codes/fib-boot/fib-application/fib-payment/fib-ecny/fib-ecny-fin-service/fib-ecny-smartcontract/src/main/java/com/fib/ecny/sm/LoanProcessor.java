package com.fib.ecny.sm;

public interface LoanProcessor<B> {
    String tranCode();

    Class<B> bodyType();

    LoanResponse process(LoanRequest request, B body, XmlUtil.TranSerialIds ids);
}
