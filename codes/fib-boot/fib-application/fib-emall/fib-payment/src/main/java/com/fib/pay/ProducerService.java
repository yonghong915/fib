package com.fib.pay;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "fib-order")
public interface ProducerService {
	@GetMapping("/echo/{str}")
	String callHello(@PathVariable String str);
}
