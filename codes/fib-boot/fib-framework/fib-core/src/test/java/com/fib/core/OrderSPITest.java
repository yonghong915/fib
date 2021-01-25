package com.fib.core;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.fib.commons.util.URL;
import com.fib.commons.compiler.Compiler;
import com.fib.commons.extension.ExtensionLoader;
import com.fib.commons.mq.MQ;
import com.fib.core.common.order.Order;

public class OrderSPITest {
	@Test
	public void test01() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);

		Order alipay = loader.getExtension("alipay");
		System.out.println(alipay.way());
		// 同一个实现类，多个名称，是同一个实例
		Order wechat = loader.getExtension("wechat");
		System.out.println(wechat.way());

	}

	@Test
	public void test02() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);

		Order adaptiveExtension = loader.getAdaptiveExtension();
		// 模拟一个URL
		URL url = URL.valueOf("xxx://localhost/ooo");
		System.out.println(adaptiveExtension.pay(url));

		url = URL.valueOf("xxx://localhost/ooo?order=wechat");
		// 这里默认是接口名，首字母小写，可以通过@Adaptive指定key
		System.out.println(adaptiveExtension.pay(url));
		System.out.println(adaptiveExtension.way());
	}

	@Test
	public void test03() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);

		Order adaptiveExtension = loader.getAdaptiveExtension();
		URL url = URL.valueOf("xxx://localhost/ooo");
		System.out.println(adaptiveExtension.pay(url));
	}

	@Test
	public void test04() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);
		URL url = URL.valueOf("xxx://localhost/ooo?pay.wechatpaykey=wechat");
		url = url.addParameter("alipaykey", "alipayvalue");
		// 激活group为online的所有扩展类
		List<Order> activateExtensions = loader.getActivateExtension(url, "", "online");
		for (Order order : activateExtensions) {
			System.out.println(order.way());
		}

		// 激活group为offline的所有扩展类
		List<Order> activateExtensions1 = loader.getActivateExtension(url, "", "offline");
		for (Order order : activateExtensions1) {
			System.out.println(order.way());
		}
	}

	@Test
	public void test05() {
		ExtensionLoader<Order> loader = ExtensionLoader.getExtensionLoader(Order.class);
		Set<String> extensions = loader.getSupportedExtensions();
		System.out.println(extensions);
	}

	@Test
	public void test06() {
		MQ c = ExtensionLoader.getExtensionLoader(MQ.class).getDefaultExtension();
		System.out.println(c);
		
	}
}
