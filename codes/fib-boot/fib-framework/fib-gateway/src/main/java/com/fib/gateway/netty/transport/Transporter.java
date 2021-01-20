package com.fib.gateway.netty.transport;

import com.fib.common.URL;
import com.fib.commons.extension.Adaptive;
import com.fib.commons.extension.SPI;
import com.fib.gateway.netty.client.Client;
import com.fib.gateway.netty.common.Constants;
import com.fib.gateway.netty.server.Server;

@SPI("netty")
public interface Transporter {

	@Adaptive({ Constants.SERVER_KEY, Constants.TRANSPORTER_KEY })
	Server bind(URL url);

	@Adaptive({ Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY })
	Client connect(URL url);
}
