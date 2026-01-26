package com.fib.order.ctrller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;
import com.fib.order.entity.OrderEntity;
import com.fib.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderCtrller {

	private OrderService orderService;

	public OrderCtrller(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("createOrder")
	public ResultRsp<Boolean> createOrder(@RequestBody OrderEntity orderEntity) {
		boolean result = orderService.createOrder(orderEntity);
		return ResultUtil.message(StatusCode.SUCCESS, result);
	}
}
