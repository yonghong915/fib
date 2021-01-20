package com.fib.core.common.order.support;

import com.fib.commons.extension.ExtensionLoader;
import com.fib.commons.util.StringUtils;
import com.fib.commons.util.URL;
import com.fib.core.common.order.Order;

//@Adaptive
public class AdaptiveOrder implements Order {
	// 用于指定要加载的扩展名称
	private String orderName;

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	@Override
	public String way() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);
		Order order;
		if (StringUtils.isEmpty(orderName)) {
			// 若没有指定请求参数，则获取默认的扩展类实例
			order = loader.getDefaultExtension();
		} else {
			// 若指定了请求参数，则获取指定的扩展类实例
			order = loader.getExtension(orderName);
		}
		return order.way();
	}

	@Override
	public String pay(URL url) {
		// TODO Auto-generated method stub
		return null;
	}
}
