package com.fib.ecny.trans.comp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderStateMachine {
    public Order processOrder(Order order, String action) {
        OrderState currentState = order.state();

        // 模式匹配 switch：编译器强制覆盖所有状态
        OrderState newState = switch (currentState) {
            case PendingPay s -> s.handle(action);
            case Paid s -> s.handle(action);
            case Shipped s -> s.handle(action);
            case Completed s -> {
                log.warn("订单 {} 已完成，忽略操作 {}", order.orderId(), action);
                yield s; // yield 用于返回值的 switch 分支
            }
            case Cancelled(String orderId, String reason) -> {
                log.warn("订单 {} 已取消: {}, 忽略操作 {}", orderId, reason, action);
                yield currentState;
            }
            case Refunded(String orderId, String reason) -> {
                log.warn("订单 {} 已退款: {}, 忽略操作 {}", orderId, reason, action);
                yield currentState;
            }
        };

        // Record Pattern：一条语句同时做类型判断 + 字段提取
        return switch (order) {
            case PhysicalOrder(var id, var st, var addr, var phone) ->
                    new PhysicalOrder(id, newState, addr, phone); // 只更新状态字段
            case VirtualOrder(var id, var st, var account) -> new VirtualOrder(id, newState, account);
        };
    }
}
