package com.fib.gateway.netty.transport;

import com.fib.common.URL;
import com.fib.commons.extension.ExtensionLoader;
import com.fib.gateway.netty.client.Client;
import com.fib.gateway.netty.server.Server;

public class Transporters {
	private Transporters() {
	}

	public static Server bind() {
		return bind();
	}

//	public static Server bind(URL url, ChannelHandler... handlers) throws RemotingException {
//		if (url == null) {
//			throw new IllegalArgumentException("url == null");
//		}
//		if (handlers == null || handlers.length == 0) {
//			throw new IllegalArgumentException("handlers == null");
//		}
//		ChannelHandler handler;
//		if (handlers.length == 1) {
//			handler = handlers[0];
//		} else {
//			handler = new ChannelHandlerDispatcher(handlers);
//		}
//		return getTransporter().bind(url, handler);
//	}
//
//	public static Client connect(String url, ChannelHandler... handler) throws RemotingException {
//		return connect(URL.valueOf(url), handler);
//	}
//
//	public static Client connect(URL url, ChannelHandler... handlers) throws RemotingException {
//		if (url == null) {
//			throw new IllegalArgumentException("url == null");
//		}
//		ChannelHandler handler;
//		if (handlers == null || handlers.length == 0) {
//			handler = new ChannelHandlerAdapter();
//		} else if (handlers.length == 1) {
//			handler = handlers[0];
//		} else {
//			handler = new ChannelHandlerDispatcher(handlers);
//		}
//		return getTransporter().connect(url, handler);
//	}

	public static Transporter getTransporter() {
		return ExtensionLoader.getExtensionLoader(Transporter.class).getAdaptiveExtension();
	}

	public static void main(String[] args) {
		Transporter transporter = getTransporter();
		URL url = URL.valueOf("127.0.0.1:9876");
		Client client = transporter.connect(url);
		client.reconnect();
	}
}
