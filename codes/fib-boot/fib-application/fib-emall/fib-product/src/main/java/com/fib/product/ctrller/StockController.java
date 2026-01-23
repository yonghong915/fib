package com.fib.product.ctrller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.web.ResultRsp;
import com.fib.product.service.StockService;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;

@RestController
@RequestMapping("/stock")
public class StockController {
	private static final Logger log = LoggerFactory.getLogger(StockController.class);
	@Resource
	private StockService stockService;

	// 扣减库存接口
	@PostMapping("/deduct/{productId}/{count}")
	@GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 30000)
	public ResultRsp<Boolean> deductStock(@PathVariable Long productId, @PathVariable Integer count) {
		log.info("开始创建订单，全局事务ID：{}", RootContext.getXID());
		boolean result = stockService.deductStock(productId, count);
		return ResultRsp.success(result);
	}
}