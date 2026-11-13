package com.fib.ecny.sm;

import com.fib.ecny.sm.serial.TranSerialIds;

public class LoanPayProcessor implements LoanProcessor<LoanAppReqBody>{
    @Override
    public String tranCode() {
        return LoanTranCode.LOAN_PAY;
    }

    @Override
    public Class<LoanAppReqBody> bodyType() {
        return LoanAppReqBody.class;
    }

    @Override
    public LoanResponse process(LoanRequest request, LoanAppReqBody body, TranSerialIds ids) {
        return null;
    }
}
