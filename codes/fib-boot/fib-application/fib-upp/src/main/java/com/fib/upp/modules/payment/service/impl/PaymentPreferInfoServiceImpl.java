package com.fib.upp.modules.payment.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.modules.payment.entity.PaymentPreferInfo;
import com.fib.upp.modules.payment.mapper.PaymentPreferInfoMapper;
import com.fib.upp.modules.payment.service.IPaymentPreferInfo;

/**
 * 支付服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-22 15:49:11
 */
@Service("paymentPreferInfoService")
public class PaymentPreferInfoServiceImpl extends ServiceImpl<PaymentPreferInfoMapper, PaymentPreferInfo>
		implements IPaymentPreferInfo {

}
