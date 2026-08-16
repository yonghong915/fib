package com.fib.ecny.order.application;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCompletedEvent {
    private String orderId;
}
