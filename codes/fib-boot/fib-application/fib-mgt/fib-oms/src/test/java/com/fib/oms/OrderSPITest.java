package com.fib.oms;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.jupiter.api.Test;

public class OrderSPITest {
	@Test
	public void test01() {
		ExtensionLoader<OrderService> loader = ExtensionLoader.getExtensionLoader(OrderService.class);

		OrderService alipayService = loader.getExtension("alipay");

		OrderService adaptiveExtension = loader.getAdaptiveExtension();

		OrderService c = ExtensionLoader.getExtensionLoader(OrderService.class).getDefaultExtension();
	}
}
