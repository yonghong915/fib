package com.fib.core.common.compiler.support;

import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

public class OrderWrapper implements Order {

	private Order order;

	public OrderWrapper(Order order) {
		this.order = order;
	}

	@Override
	public String way() {
		System.out.println("before-这是wrapper对way()的增强");
		String way = order.way();
		System.out.println("after-这是wrapper对way()的增强");
		return way;
	}

	@Override
	public String pay(URL url) {
		System.out.println("before-这是wrapper对pay()的增强");
		String pay = order.pay(url);
		System.out.println("after-这是wrapper对pay()的增强");
		return pay;
	}

}
