package com.fib.product.ctrller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;
import com.fib.product.entity.ProductEntity;
import com.fib.product.service.IProductService;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductCtrller {
	private static final Logger log = LoggerFactory.getLogger(ProductCtrller.class);
	@Resource
	private IProductService stockService;

	// 扣减库存接口
	@PostMapping("/deductProduct")
	@GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 30000)
	public ResultRsp<Boolean> deductProduct(@RequestBody ProductEntity productEntity) {
		log.info("开始创建订单，全局事务ID：{}", RootContext.getXID());
		boolean result = stockService.deductStock(productEntity);
		return ResultUtil.message(StatusCode.SUCCESS, result);
	}
}