package com.fib.ecny.sm;

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
    public LoanResponse process(LoanRequest request, LoanAppReqBody body, XmlUtil.TranSerialIds ids) {
        return null;
    }
}
