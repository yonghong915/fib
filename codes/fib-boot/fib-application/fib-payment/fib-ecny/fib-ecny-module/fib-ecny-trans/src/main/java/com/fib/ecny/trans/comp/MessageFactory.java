package com.fib.ecny.trans.comp;

import java.math.BigDecimal;

public class MessageFactory {
    public void handleMessage(Message msg) {
        String result = switch (msg) {
            case OrderMessage om when (om.getTranAmt().compareTo(new BigDecimal("10000"))) > 0 -> "大额订单";
            case OrderMessage om -> "普通订单" + om.getTranAmt();
            case ImageMessage im -> "";
            case TextMessage tm -> "";
            default -> throw new IllegalStateException("Unexpected value: " + msg);
        };
    }
}
