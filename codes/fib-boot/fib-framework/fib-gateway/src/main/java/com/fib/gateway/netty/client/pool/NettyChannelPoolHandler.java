package com.fib.gateway.netty.client.pool;

import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.Serializer;
import com.fib.commons.serializer.protostuff.ProtoStuffSerializer;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.protocol.CustomMessageDecoder;
import com.fib.gateway.netty.protocol.CustomMessageEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChannelPoolHandler implements ChannelPoolHandler {
	@Override
	public void channelReleased(Channel ch) throws Exception {
		System.out.println("channelReleased. Channel ID: " + ch.id());
		ch.writeAndFlush(Unpooled.EMPTY_BUFFER);
	}

	@Override
	public void channelAcquired(Channel ch) throws Exception {
		System.out.println("channelAcquired. Channel ID: " + ch.id());
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
		System.out.println("channelCreated. Channel ID: " + ch.id());
		Serializer serializer = SerializationUtils.getInstance().loadSerializerInstance(ProtoStuffSerializer.class);
		SocketChannel channel = (SocketChannel) ch;
		channel.config().setKeepAlive(true);
		channel.config().setTcpNoDelay(true);
		channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter)).addLast(new StringDecoder())
				.addLast(new StringEncoder()).addLast(new NettyClientHander());
		channel.pipeline().addLast(new CustomMessageEncoder(TranslatorData.class, serializer));
		channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
		channel.pipeline().addLast(new CustomMessageDecoder(TranslatorData.class, serializer));
	}

}
