package com.fib.tcp.socket.netty;

import org.springframework.cache.annotation.EnableCaching;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ChinaClientHandler extends SimpleChannelInboundHandler {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client channelActive..");
		// ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
		// // 必须有flush
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,I amd server.", CharsetUtil.UTF_8));
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush("hello ,I amd server." + i + "end\n");
		}
		
		// 必须存在flush
		// ctx.write(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
		// ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("client channelRead..");
		System.out.println("recie msg:" + msg);
	}

}
