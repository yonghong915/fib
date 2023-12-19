//package com.fib.tcp.socket.netty;
//
//import java.lang.reflect.Proxy;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//
//public class NettyClient {
//	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//	private static NettyClientHandler clientHandler;
//
//	/**
//	 * 代理模式
//	 * 
//	 * @param serviceClass 接口类
//	 * @param providerName 协议头
//	 * @return
//	 */
//	public Object getBean(final Class<?> serviceClass, final String providerName) {
//		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { serviceClass }, (proxy, method, args) -> {
//			if (clientHandler == null) {
//				initClient();
//			}
//			// 设置要发给服务端的信息
//			clientHandler.setParam(providerName + args[0]);
//			return executor.submit(clientHandler).get();
//		});
//
//	}
//
//	/**
//	 * 初始化客户端
//	 */
//	private static void initClient() {
//		try {
//			clientHandler = new NettyClientHandler();
//			NioEventLoopGroup group = new NioEventLoopGroup();
//			Bootstrap bootstrap = new Bootstrap();
//			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
//					.handler(new ChannelInitializer<SocketChannel>() {
//						@Override
//						protected void initChannel(SocketChannel ch) throws Exception {
//							ChannelPipeline pipeline = ch.pipeline();
//							pipeline.addLast(new StringDecoder());
//							pipeline.addLast(new StringEncoder());
//							pipeline.addLast(clientHandler);
//						}
//					});
//			bootstrap.connect("127.0.0.1", 8081).sync();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//}
