package com.fib.ecny.trans.comp;

public record Cancelled(String orderId, String reason) implements OrderState {
    @Override
    public OrderState handle(String action) {
        throw new IllegalStateException("订单已取消，不可操作");
    }
}