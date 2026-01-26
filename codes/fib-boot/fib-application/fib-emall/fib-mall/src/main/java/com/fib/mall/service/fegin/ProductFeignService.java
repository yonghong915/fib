package com.fib.mall.service.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.fib.core.web.ResultRsp;
import com.fib.mall.dto.ProductDto;

@FeignClient(name = "fib-product")
public interface ProductFeignService {
	@PostMapping(value = "/product/deductProduct", consumes = "application/json")
	ResultRsp<?> deductProduct(ProductDto productDto);
}
