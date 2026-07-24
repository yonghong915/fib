package com.fib.ecny.trans.comp;

public record Shipped(String orderId) implements OrderState {
    @Override
    public OrderState handle(String action) {
        return switch (action) {
            case "confirm" -> new Completed(orderId);
            default -> throw new IllegalStateException(
                    "已发货状态下不支持操作: " + action);
        };
    }
}