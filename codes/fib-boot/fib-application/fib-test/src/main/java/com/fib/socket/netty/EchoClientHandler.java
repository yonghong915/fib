//package com.fib.socket.netty;
//
//import java.nio.charset.Charset;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufUtil;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.util.CharsetUtil;
//
//public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
//
//	/**
//	 * 向服务端发送数据
//	 */
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "通道开启！");
//
//		String sendInfo = "你好，我是客户端！";
//		System.out.println("客户端向服务端发送数据：" + sendInfo);
//		ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush
//
//	}
//
//	/**
//	 * channelInactive
//	 *
//	 * channel 通道 Inactive 不活跃的
//	 *
//	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
//	 *
//	 */
//	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "通道关闭！");
//	}
//
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//		ByteBuf buf = msg.readBytes(msg.readableBytes());
//		System.out.println("收到服务端反馈信息:" + ByteBufUtil.hexDump(buf) + "; 数据包为:" + buf.toString(Charset.forName("utf-8")));
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		ctx.close();
//		System.out.println("异常退出:" + cause.getMessage());
//	}
//}
