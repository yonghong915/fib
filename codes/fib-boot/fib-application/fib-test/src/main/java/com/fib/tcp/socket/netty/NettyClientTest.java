//package com.fib.tcp.socket.netty;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//import io.netty.handler.codec.LengthFieldPrepender;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.util.CharsetUtil;
//
//public class NettyClientTest {
//	/**
//	 * Netty服务端的端口
//	 */
//	private static final int PORT = 28888;
//
//	/**
//	 * 服务端接收连接队列的长度
//	 */
//	private static final int QUEUE_SIZE = 1024 * 1024;
//
//	/**
//	 * 粘包的分隔符
//	 */
//	private static final String DELIMITER = "\r\n";
//
//	/**
//	 * 分隔的最大长度
//	 */
//	private static final int DELIMITER_MAX_LENGTH = 1024;
//
//	public static void main(String[] args) {
//		// 客户端的eventLoop
//		EventLoopGroup group = new NioEventLoopGroup();
//
//		Bootstrap b = new Bootstrap();
//		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
//			@Override
//			public void initChannel(SocketChannel ch) throws Exception {
//				ChannelPipeline p = ch.pipeline();
//				// 添加日志处理器
////				p.addLast(new LoggingHandler(LogLevel.INFO));
////				p.addLast(new DelimiterBasedFrameDecoder(DELIMITER_MAX_LENGTH, Unpooled.wrappedBuffer(DELIMITER.getBytes(StandardCharsets.UTF_8))));
////				// 字符串编解码器
////				p.addLast(new StringDecoder(StandardCharsets.UTF_8), new StringEncoder(StandardCharsets.UTF_8));
//
//				// 数据分包，组包，粘包
//				p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//				p.addLast(new LengthFieldPrepender(4));
//
//				p.addLast(new StringDecoder(CharsetUtil.UTF_8));
//				p.addLast(new StringEncoder(CharsetUtil.UTF_8));
//				p.addLast(new ChinaClientHandler()); 
//			}
//		});
//		// 启动客户端
//		ChannelFuture f;
//		try {
//			f = b.connect("127.0.0.1", 8000).sync();
//
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//}
