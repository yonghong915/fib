package com.fib.order.ctrller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.web.ResultRsp;
import com.fib.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderCtrller {

	private OrderService orderService;

	public OrderCtrller(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/deduct/{productId}/{count}/{userId}")
	public ResultRsp<Boolean> deductStock(@PathVariable Long productId, @PathVariable Integer count, Long userId) {
		boolean result = orderService.createOrder(productId, count, userId);
		return ResultRsp.success(result);
	}
}
