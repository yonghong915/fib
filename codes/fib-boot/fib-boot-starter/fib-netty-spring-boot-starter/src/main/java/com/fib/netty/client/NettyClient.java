package com.fib.netty.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.netty.DefaultFuture;
import com.fib.netty.vo.Request;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
	private ChannelFuture channelFuture;

	@Resource
	private NettyClientInitializer nettyClientInitializer;

	public ChannelFuture connect(String host, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).handler(nettyClientInitializer);
			channelFuture = bootstrap.connect(host, port).sync();
		} finally {
			//
		}
		return channelFuture;
	}

	private ExecutorService executor = Executors.newFixedThreadPool(10);

	public Object send(Object msg) throws InterruptedException {
		Request request = new Request();
		request.setRequestMsg(msg);
		LOGGER.info("Request:[{}],now is={}", request, LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyy-MM-dd HH:mm:ss,SSS"));

		CompletableFuture<Object> defaultFuture = DefaultFuture.newFuture(channelFuture.channel(), request, 30000, executor);
		channelFuture.channel().writeAndFlush(request);
		Object result = null;
		try {
			result = defaultFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		LOGGER.info("Request:[{}],Response:[{}],now is = {}", request, result,
				LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyy-MM-dd HH:mm:ss,SSS"));
		return result;
	}
}