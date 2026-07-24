package com.fib.ecny.trans.comp;

public record Completed(String orderId) implements OrderState {
    @Override
    public OrderState handle(String action) {
        // 已完成状态不接受任何操作
        throw new IllegalStateException("订单已完成，不可操作");
    }
}