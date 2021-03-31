package com.fib.upp.pay.strategy;

public class UserContext {
	private PaymentStrategy paymentStrategy;

	public UserContext() {
	}

	public UserContext(PaymentStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}

	public PaymentStrategy getPaymentStrategy() {
		return paymentStrategy;
	}

	public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}

	public void pay(int amount) {
		paymentStrategy.pay(amount);
	}
}