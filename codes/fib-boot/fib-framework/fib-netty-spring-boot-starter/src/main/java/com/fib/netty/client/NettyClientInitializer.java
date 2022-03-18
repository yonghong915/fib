package com.fib.netty.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.netty.codec.ProtoStuffDecoder;
import com.fib.netty.codec.ProtoStuffEncoder;
import com.fib.netty.vo.Request;
import com.fib.netty.vo.Response;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

@Component
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private NettyClientHandler nettyClientHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new ProtoStuffDecoder(Response.class));
		ch.pipeline().addLast(new ProtoStuffEncoder(Request.class));
		ch.pipeline().addLast(nettyClientHandler);
	}
}
