package com.fib.gateway.netty.server;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.CommonException;
import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.Serializer;
import com.fib.commons.serializer.protostuff.ProtoStuffSerializer;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.protocol.CustomMessageDecoder;
import com.fib.gateway.netty.protocol.CustomMessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Netty服务器端
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
@Service("nettyServer")
public class NettyServer extends AbstractServer {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static EventLoopGroup bossGroup; // 通过nio方式来接收连接和处理连接
	private static EventLoopGroup workerGroup; // 通过nio方式来接收连接和处理连接
	private static ServerBootstrap bootstrap;
	private Channel channel;
	private Map<String, Channel> channels; // <ip:port, channel>

	@Autowired
	private NettyServerHandler nettyServerHandler;

	public void start(int port) {
		try {
			bootstrap = new ServerBootstrap();
			bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
			workerGroup = new NioEventLoopGroup(8, new DefaultThreadFactory("NettyServerWorker", true));

			channels = nettyServerHandler.getChannels();

			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
					.childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					// 保持长连接
					.childOption(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline cp = ch.pipeline();
							// 入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
							Serializer serializer = SerializationUtils.getInstance()
									.loadSerializerInstance(ProtoStuffSerializer.class);
							cp.addLast(new IdleStateHandler(40, 0, 0, TimeUnit.SECONDS));
							cp.addLast(new CustomMessageEncoder(TranslatorData.class, serializer));
							cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
							cp.addLast(new CustomMessageDecoder(TranslatorData.class, serializer));
							// 业务逻辑实现类
							cp.addLast("nettyServerHandler", nettyServerHandler);
						}
					}); // 设置过滤器
			// 服务器绑定端口监听 绑定端口，同步等待成功
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			logger.info("Server Startup...,port:{}", port);

			channelFuture.syncUninterruptibly();
			channel = channelFuture.channel();
			// 等待服务器监听端口关闭
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("Failed to start Netty Server.", e);
			throw new CommonException("Failed to start Netty Server.", e);
		} finally {
			// 关闭EventLoopGroup，释放掉所有资源包括创建的线程
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			logger.info("Sever ShutDown...");
		}
	}

	private void close() {
		if (channel != null) {
			channel.close();
		}

		if (bootstrap != null) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

		if (channels != null) {
			channels.clear();
		}
	}
}
