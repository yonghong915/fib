package com.fib.order.service;

import com.fib.order.entity.OrderEntity;

public interface OrderService {
	boolean createOrder(OrderEntity orderEntity);
}
