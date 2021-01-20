package com.fib.core.common.order.support;

import com.fib.commons.extension.Activate;
import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

@Activate(group = "online", value = "alipaykey")
public class AlipayOrder implements Order {
	@Override
	public String way() {
		System.out.println("--- 支付宝way() ---");
		return "支付宝支付方式";
	}

	@Override
	public String pay(URL url) {
		System.out.println("--- 支付宝pay() ---");
        return "使用支付宝支付";
	}

}
