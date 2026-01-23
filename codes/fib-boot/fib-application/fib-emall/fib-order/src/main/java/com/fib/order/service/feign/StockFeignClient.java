package com.fib.order.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fib.core.web.ResultRsp;

@FeignClient(name = "fib-product")
public interface StockFeignClient {
	@PostMapping("/stock/deduct/{productId}/{count}")
	ResultRsp<Boolean> deductStock(@PathVariable("productId") Long productId, @PathVariable("count") Integer count);

}
