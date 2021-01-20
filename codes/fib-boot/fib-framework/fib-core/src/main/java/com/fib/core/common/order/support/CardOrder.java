package com.fib.core.common.order.support;

import com.fib.commons.extension.Activate;
import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

@Activate(group = {"online", "offline"}, order = 3)
public class CardOrder implements Order {
    @Override
    public String way() {
        System.out.println("--- 银行卡way() ---");
        return "银行卡支付方式";
    }

	@Override
	public String pay(URL url) {
		// TODO Auto-generated method stub
		return null;
	}
}
