package com.fib.ecny.trans.comp;

// 已支付状态
public record Paid(String orderId) implements OrderState {
    @Override
    public OrderState handle(String action) {
        return switch (action) {
            case "ship" -> new Shipped(orderId);
            case "refund" -> new Refunded(orderId, "已退款");
            default -> throw new IllegalStateException(
                    "已支付状态下不支持操作: " + action);
        };
    }
}