package com.fib.ecny.trans.comp;

public record VirtualOrder(
        String orderId,
        OrderState state,
        String deliveryAccount   // 发放账号（如手机号充值）
) implements Order {
}