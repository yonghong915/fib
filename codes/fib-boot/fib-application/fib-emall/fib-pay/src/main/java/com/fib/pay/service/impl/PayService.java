package com.fib.pay.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.pay.entity.PayEntity;
import com.fib.pay.mapper.PayMapper;
import com.fib.pay.service.IPayService;

@Service("payService")
public class PayService extends ServiceImpl<PayMapper, PayEntity> implements IPayService {

	@Override
	public boolean pay(PayEntity payEntity) {
		return true;
	}
}
