package com.fib.ecny.trans.comp;

public class OrderServiceTest {
    public static void main(String[] args) {
        // 创建一个待支付的实物订单
        Order order = new PhysicalOrder(
                "ORD-20260630-001",
                new PendingPay("ORD-20260630-001"),
                "北京市朝阳区xx路xx号",
                "13800138000"
        );

        OrderStateMachine machine = new OrderStateMachine();

        // 支付 → 已支付
        order = machine.processOrder(order, "pay");
        System.out.println("支付后: " + order.state());
        // 输出: 支付后: Paid[orderId=ORD-20260630-001]

        // 发货 → 已发货
        order = machine.processOrder(order, "ship");
        System.out.println("发货后: " + order.state());
        // 输出: 发货后: Shipped[orderId=ORD-20260630-001]

        // 确认收货 → 已完成
        order = machine.processOrder(order, "confirm");
        System.out.println("确认后: " + order.state());
        // 输出: 确认后: Completed[orderId=ORD-20260630-001]

        // 测试终态拒绝操作
        try {
            machine.processOrder(order, "refund");
        } catch (IllegalStateException e) {
            System.out.println("预期异常: " + e.getMessage());
            // 输出: 预期异常: 订单已完成，不可操作
        }
    }
}
