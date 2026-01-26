package com.fib.mall.ctrller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;
import com.fib.mall.entity.MallEntity;
import com.fib.mall.service.IMallService;

@RestController
@RequestMapping("/mall")
public class MallCtrller {
	private IMallService mallService;

	public MallCtrller(IMallService mallService) {
		this.mallService = mallService;
	}

	@GetMapping("/deduct/{productId}/{count}/{userId}")
	public ResultRsp<Boolean> deductStock(@PathVariable Long productId, @PathVariable Integer count,
			@PathVariable Long userId) {
		MallEntity mallEntity = new MallEntity();
		mallEntity.setProductId(productId);
		mallEntity.setStockCount(count);
		boolean result = mallService.create(mallEntity);
		return ResultUtil.message(StatusCode.SUCCESS, result);
	}
}