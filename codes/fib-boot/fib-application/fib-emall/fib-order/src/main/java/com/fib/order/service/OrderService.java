package com.fib.order.service;

public interface OrderService {
	boolean createOrder(Long productId, Integer count, Long userId);
}
