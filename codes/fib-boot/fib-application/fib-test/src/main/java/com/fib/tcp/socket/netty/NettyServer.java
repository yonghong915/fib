//package com.fib.tcp.socket.netty;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//
//public class NettyServer {
//	/**
//	 * 对外暴露的方法
//	 *
//	 * @param hostName 主机名
//	 * @param port     端口号
//	 */
//	public static void startServer(String hostName, int port) {
//		startServer0(hostName, port);
//	}
//
//	private static void startServer0(String hostName, int port) {
//		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//		EventLoopGroup workerGroup = new NioEventLoopGroup();
//		try {
//			ServerBootstrap serverBootstrap = new ServerBootstrap();
//			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) throws Exception {
//					ChannelPipeline pipeline = ch.pipeline();
//					pipeline.addLast(new StringDecoder());
//					pipeline.addLast(new StringEncoder());
//					pipeline.addLast(new NettyServerHandler());
//				}
//			});
//			ChannelFuture channelFuture = serverBootstrap.bind(hostName, port).sync();
//			System.out.println("<===========服务器启动成功，端口号：" + port + "=============>");
//			channelFuture.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} finally {
//			bossGroup.shutdownGracefully();
//			workerGroup.shutdownGracefully();
//		}
//	}
//}
