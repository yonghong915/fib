package com.fib.gateway.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fib.commons.exception.CommonException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class NettyChannel {
	private static final ConcurrentMap<Channel, NettyChannel> channelMap = new ConcurrentHashMap<Channel, NettyChannel>();
	private final Channel channel;

	private NettyChannel(Channel channel) {
		this.channel = channel;
	}

	static NettyChannel getOrAddChannel(Channel ch) {
		if (ch == null) {
			return null;
		}
		NettyChannel ret = channelMap.get(ch);
		if (ret == null) {
			NettyChannel nettyChannel = new NettyChannel(ch);
			if (ch.isActive()) {
				ret = channelMap.putIfAbsent(ch, nettyChannel);
			}
			if (ret == null) {
				ret = nettyChannel;
			}
		}
		return ret;
	}

	static void removeChannelIfDisconnected(Channel ch) {
		if (ch != null && !ch.isActive()) {
			channelMap.remove(ch);
		}
	}

	public void send(Object message, boolean sent) {
		boolean success = true;
		int timeout = 0;
		try {
			ChannelFuture future = channel.writeAndFlush(message);
			if (sent) {
				timeout = 5000;
				success = future.await(timeout);
			}
			Throwable cause = future.cause();
			if (cause != null) {
				throw cause;
			}
		} catch (Throwable e) {
			throw new CommonException("Failed to send message " + message + " to ");
		}
		if (!success) {
			throw new CommonException("Failed to send message ");
		}
	}
}
