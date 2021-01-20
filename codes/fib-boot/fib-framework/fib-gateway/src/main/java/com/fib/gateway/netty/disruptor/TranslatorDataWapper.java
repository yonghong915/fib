package com.fib.gateway.netty.disruptor;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
public class TranslatorDataWapper {
	// 实际的数据
	private TranslatorData data;
	// ctx对象
	private ChannelHandlerContext ctx;

	public TranslatorData getData() {
		return data;
	}

	public void setData(TranslatorData data) {
		this.data = data;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
}
