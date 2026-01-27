package com.fib.order.ctrller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;
import com.fib.order.annotation.FilterProcess;
import com.fib.order.entity.OrderEntity;
import com.fib.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderCtrller {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCtrller.class);
	private OrderService orderService;

	public OrderCtrller(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("createOrder")
	@FilterProcess
	public ResultRsp<?> createOrder(@RequestBody OrderEntity orderEntity) {
		LOGGER.info("orderEntity={}", orderEntity);
		orderService.createOrder(orderEntity);
		return ResultUtil.message(StatusCode.SUCCESS, "调用成功");
	}
}
