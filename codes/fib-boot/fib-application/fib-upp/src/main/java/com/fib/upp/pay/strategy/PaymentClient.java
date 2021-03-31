package com.fib.upp.pay.strategy;

public class PaymentClient {
	public static void main(String[] args) {
		// 创建用户
		UserContext userContext = new UserContext();
		// 创建策略
		AliPaymentStrategy aliPaymentStrategy = new AliPaymentStrategy("1", "2", "3", "4");
		userContext.setPaymentStrategy(aliPaymentStrategy);

		// Ali 支付
		userContext.pay(100);
	}
}
