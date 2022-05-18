package com.fib.netty.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelId;

public enum Sessions {

	INSTANCE;

	Sessions() {
		sessionMap = new ConcurrentHashMap<>();
	}

	private final Map<ChannelId, Session> sessionMap;

	public Session get(ChannelId id) {
		return sessionMap.get(id);
	}

	public void register(ChannelId id, Session session) {
		sessionMap.put(id, session);
	}

	public Session unregister(ChannelId id) {
		return sessionMap.remove(id);
	}
}