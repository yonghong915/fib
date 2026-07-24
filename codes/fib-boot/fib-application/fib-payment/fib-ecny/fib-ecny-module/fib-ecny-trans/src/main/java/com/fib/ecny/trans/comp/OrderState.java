package com.fib.ecny.trans.comp;

public sealed interface OrderState
        permits PendingPay, Paid, Shipped, Completed, Cancelled, Refunded {
    // 每个状态都要实现：接收动作，返回新状态
    OrderState handle(String action);
}
