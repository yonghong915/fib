package com.fib.tcp.socket.netty;

import java.util.concurrent.Callable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {
	/**
	 * 上下文
	 */
	private ChannelHandlerContext context;
	/**
	 * 返回的结果
	 */
	private String result;
	/**
	 * 客户端调用服务时传入的方法参数
	 */
	private String param;

	/**
	 * 被代理对象调用，发送数据给服务端，并等待服务端返回结果
	 *
	 * @return result
	 * @throws Exception 异常
	 */
	@Override
	public synchronized String call() throws Exception {
		/*
		 * 向服务端发送消息
		 */
		context.writeAndFlush(param);
		// 开始等待,直到服务端返回结果，被唤醒
		wait();
		return result;
	}

	/**
	 * 客户端连接成功后，向服务端发送消息
	 *
	 * @param ctx 上下文
	 * @throws Exception 异常
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		context = ctx;
	}

	/**
	 * 客户端接收服务端返回的消息
	 *
	 * @param ctx 上下文
	 * @param msg 消息
	 * @throws Exception 异常
	 */
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		result = msg.toString();
		// 唤醒等待的线程
		notify();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	public void setParam(String param) {
		this.param = param;
	}
}
