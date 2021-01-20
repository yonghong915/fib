package com.fib.gateway.netty.transport.netty4;

import com.fib.common.URL;
import com.fib.gateway.netty.client.Client;
import com.fib.gateway.netty.client.NettyClient;
import com.fib.gateway.netty.server.NettyServer;
import com.fib.gateway.netty.server.Server;
import com.fib.gateway.netty.transport.Transporter;

public class NettyTransporter implements Transporter {

	@Override
	public Server bind(URL url) {
		return new NettyServer();
	}

	@Override
	public Client connect(URL url) {
		return new NettyClient();
	}
}
