//package com.fib.tcp.socket.netty;
//
//import com.fib.tcp.socket.service.HelloServiceImpl;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
//	/**
//	 * 当有客户端连接进来的时候，调用此方法
//	 *
//	 * @param ctx 当前连接的上下文
//	 * @param msg 当前连接的消息
//	 * @throws Exception 异常
//	 */
//	@Override
//	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		// 获取客户端发送的消息，并调用服务
//		System.out.println("服务端Handler收到客户端消息：{" + msg + "}");
//		// 客户端在调用服务器的api时，需要遵守相应的协议
//		// 协议：每次发的消息必须以"HelloService#hello#"开头
//		if (msg.toString().startsWith("HelloService#hello#")) {
//			// 调用方法
//			String response = new HelloServiceImpl().sayHello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
//			ctx.writeAndFlush(response);
//
//		}
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		super.exceptionCaught(ctx, cause);
//	}
//}
