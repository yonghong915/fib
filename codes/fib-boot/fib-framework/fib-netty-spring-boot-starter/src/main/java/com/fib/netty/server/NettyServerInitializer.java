package com.fib.netty.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.netty.codec.ProtoStuffDecoder;
import com.fib.netty.codec.ProtoStuffEncoder;
import com.fib.netty.vo.Request;
import com.fib.netty.vo.Response;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private NettyServerHandler nettyServerHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new ProtoStuffDecoder(Request.class));
		ch.pipeline().addLast(new ProtoStuffEncoder(Response.class));
		ch.pipeline().addLast(nettyServerHandler);
	}
}
