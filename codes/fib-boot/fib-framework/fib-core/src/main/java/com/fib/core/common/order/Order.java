package com.fib.core.common.order;

import com.fib.commons.extension.Adaptive;
import com.fib.commons.extension.SPI;
import com.fib.commons.util.URL;

@SPI("alipay")
public interface Order {
	String way();

	@Adaptive
	String pay(URL url);
}
