package com.fib.ecny.trans.comp;

public record Refunded(String orderId, String reason) implements OrderState {
    @Override
    public OrderState handle(String action) {
        throw new IllegalStateException("订单已退款，不可操作");
    }
}