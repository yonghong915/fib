package com.fib.mall.service.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fib.core.web.ResultRsp;
import com.fib.mall.dto.PayDto;

@FeignClient(name = "fib-payment")
public interface PayFeignService {

	@PostMapping(value = "/pay", consumes = "application/json")
	ResultRsp<?> pay(@RequestBody PayDto payDto);
}
