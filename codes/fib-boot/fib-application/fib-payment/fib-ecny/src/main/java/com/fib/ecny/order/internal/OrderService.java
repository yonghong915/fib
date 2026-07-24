package com.fib.ecny.order.internal;

import com.fib.ecny.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ApplicationEventPublisher publisher;

    public void create() {
        // 业务逻辑
        publisher.publishEvent(new OrderCreatedEvent(1L, 1001L));
    }
}
