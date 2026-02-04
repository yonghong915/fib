package com.fib.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.pay.entity.PayEntity;

public interface IPayService extends IService<PayEntity>{

	boolean pay(PayEntity payEntity);

}
