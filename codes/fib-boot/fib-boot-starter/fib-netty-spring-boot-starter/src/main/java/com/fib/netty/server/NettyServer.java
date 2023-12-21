package com.fib.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.Resource;

@Component
public class NettyServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

	@Resource
	private NettyServerInitializer nettyServerInitializer;

	@Value("${netty.port:8088}")
	private int port;

	public void start() {
		init();
	}

	private void init() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).childOption(ChannelOption.SO_SNDBUF, 128 * 1024).childOption(ChannelOption.SO_RCVBUF, 128 * 1024)
				.childOption(ChannelOption.SO_KEEPALIVE, false).channel(NioServerSocketChannel.class).option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.SO_BACKLOG, 1024).childHandler(nettyServerInitializer);
		ChannelFuture future = null;
		try {
			future = bootstrap.bind(port).sync();
			LOGGER.info("Server started and listen on {}", future.channel().localAddress());
			future.channel().closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
					LOGGER.info("Channel {} closed.", future.channel());
				}
			});
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted!", e);
			Thread.currentThread().interrupt();
		}
	}
}