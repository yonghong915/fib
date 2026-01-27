package com.fib.mall.service.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fib.core.web.ResultRsp;
import com.fib.mall.config.FeignGlobalConfig;
import com.fib.mall.dto.OrderDto;

@FeignClient(name = "fib-order", configuration = FeignGlobalConfig.class)
public interface OrderFeignService {
	@PostMapping(value = "/order/createOrder", consumes = "application/json")
	ResultRsp<?> createOrder(@RequestBody OrderDto orderDto);
}
