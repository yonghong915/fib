package com.fib.ecny.order.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// 订单模块发布事件
@Service
public class OrderApplicationService {
    private final ApplicationEventPublisher events;

    public OrderApplicationService(ApplicationEventPublisher events) {
        this.events = events;
    }

    public void createOrder(String orderId) {
        events.publishEvent(new OrderCompletedEvent(orderId));
    }

    // 库存模块监听事件
    @Component
    class InventoryEventListener {
        @EventListener
        @Async
        public void handleOrderCompleted(OrderCompletedEvent event) {
            // 预留库存 - 跨模块异步解耦
            //inventoryService.reserve(event.getOrderId());
        }
    }
}
