package com.fib.core.common.order.support;

import com.fib.commons.extension.Activate;
import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

@Activate(group = "offline", order = 4)
public class CashOrder implements Order {
    @Override
    public String way() {
        System.out.println("--- 现金way() ---");
        return "现金支付方式";
    }

	@Override
	public String pay(URL url) {
		// TODO Auto-generated method stub
		return null;
	}
}
