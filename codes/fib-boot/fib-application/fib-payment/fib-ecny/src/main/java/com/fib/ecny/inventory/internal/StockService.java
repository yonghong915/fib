package com.fib.ecny.inventory.internal;

import com.fib.ecny.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {

        // 扣减库存逻辑
    }
}
