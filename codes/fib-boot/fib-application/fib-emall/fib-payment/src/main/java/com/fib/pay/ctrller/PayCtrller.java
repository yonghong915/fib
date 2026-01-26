package com.fib.pay.ctrller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fib.core.util.StatusCode;
import com.fib.core.web.ResultRsp;
import com.fib.core.web.ResultUtil;
import com.fib.pay.entity.PayEntity;
import com.fib.pay.service.IPayService;

@RestController
//@RequestMapping("/pay")
public class PayCtrller {
	private IPayService payService;

	public PayCtrller(IPayService payService) {
		this.payService = payService;
	}

	@PostMapping("/pay")
	public ResultRsp<Boolean> pay(@RequestBody PayEntity payEntity) {
		boolean result = payService.pay(payEntity);
		return ResultUtil.message(StatusCode.SUCCESS, result);
	}
}
