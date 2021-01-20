package com.fib.core.common.order.support;

import com.fib.commons.extension.Activate;
import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

@Activate(group = "online", value = "pay.wechatpaykey:wechat")
public class WeChatOrder implements Order {
	@Override
	public String way() {
		System.out.println("--- 微信way() ---");
		return "微信支付方式";
	}

	@Override
	public String pay(URL url) {
		System.out.println("--- 微信pay() ---");
		return "使用微信支付";
	}

}
