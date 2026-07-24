package com.fib.ecny.trans.comp;

public record PhysicalOrder(
        String orderId,
        OrderState state,
        String shippingAddress,  // 收货地址
        String receiverPhone     // 收件人电话
) implements Order {}
