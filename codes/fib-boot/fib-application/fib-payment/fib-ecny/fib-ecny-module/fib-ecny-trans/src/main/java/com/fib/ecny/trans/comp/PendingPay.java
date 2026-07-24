package com.fib.ecny.trans.comp;

// 待支付状态
public record PendingPay(String orderId) implements OrderState {
    @Override
    public OrderState handle(String action) {
        return switch (action) {
            case "pay" -> new Paid(orderId);
            case "cancel" -> new Cancelled(orderId, "用户取消");
            default -> throw new IllegalStateException(
                    "待支付状态下不支持操作: " + action);
        };
    }
}