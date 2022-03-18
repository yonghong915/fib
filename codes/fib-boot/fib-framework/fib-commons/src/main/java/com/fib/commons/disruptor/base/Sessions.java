package com.fib.commons.disruptor.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelId;

/**
 * Simple session manager. Register session When the user's connection active.
 * and unregister session when lost connection.
 *
 * @author TinyZ on 2015/10/27.
 */
public enum Sessions {

	INSTANCE;

	Sessions() {
		sessions = new ConcurrentHashMap<>();
	}

	private final Map<ChannelId, Session> sessions;

	public Session get(ChannelId id) {
		return sessions.get(id);
	}

	public void register(ChannelId id, Session session) {
		sessions.put(id, session);
	}

	public Session unregister(ChannelId id) {
		return sessions.remove(id);
	}
}