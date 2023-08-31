package com.fib.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fib.netty.DefaultFuture;
import com.fib.netty.vo.Response;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
@Component
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		LOGGER.info("Client received: msg is : [{}]", msg);
		if (msg instanceof Response rsp) {
			DefaultFuture.received(rsp);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.warn("Unexpected exception from downstream.", cause);
		ctx.close();
	}
}