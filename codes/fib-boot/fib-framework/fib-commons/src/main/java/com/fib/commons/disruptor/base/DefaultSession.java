package com.fib.commons.disruptor.base;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * Default session implement.
 */
public class DefaultSession implements Session {

	private ChannelHandlerContext ctx;

	public DefaultSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public ChannelHandlerContext ctx() {
		return ctx;
	}

	@Override
	public boolean isOnline() {
		return ctx != null && ctx.channel().isActive();
	}


	@Override
	public void writeAndFlush(Object msg) {
		writeAndFlush(msg, null);
	}

	@Override
	public void writeAndFlush(Object message, ChannelFutureListener listener) {
		if (null == ctx || ctx.channel() == null || !ctx.channel().isActive()) {
			return;
		}
		if (ctx.channel().isWritable()) {
			if (listener == null) {
				ctx.channel().writeAndFlush(message);
			} else {
				ctx.writeAndFlush(message).addListener(listener);
			}
		} else {
			ctx.channel().eventLoop().schedule(() -> {
				writeAndFlush(message, listener);
			}, 1L, TimeUnit.SECONDS);
		}
	}

	@Override
	public void offline() {
		if (ctx != null) {
			ctx.close();
		}
	}

	@Override
	public void release() {
		ctx = null;
	}
}
