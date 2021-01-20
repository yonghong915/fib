package com.fib.gateway.netty.client.pool;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHander extends ChannelInboundHandlerAdapter {
	static AtomicInteger count = new AtomicInteger(1);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(count.getAndIncrement() + ":" + msg);
	}

}
