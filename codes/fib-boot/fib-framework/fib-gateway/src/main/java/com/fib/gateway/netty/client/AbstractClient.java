package com.fib.gateway.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.Channel;

public abstract class AbstractClient implements Client {
	private final Lock connectLock = new ReentrantLock();

	public AbstractClient() {
		doOpen();
		connect();
	}

	protected abstract Channel getChannel();

	@Override
	public void reconnect() {
		disconnect();
		connect();
	}

	protected void connect() {
		connectLock.lock();
		try {
			if (isConnected()) {
				return;
			}
			doConnect();
		} catch (Exception e) {
			throw e;
		} finally {
			connectLock.unlock();
		}
	}

	public boolean isConnected() {
		Channel channel = getChannel();
		if (channel == null)
			return false;
		return channel.isActive();
	}

	public void disconnect() {
		connectLock.lock();
		try {
			try {
				doDisConnect();
			} catch (Throwable e) {

			}
		} finally {
			connectLock.unlock();
		}
	}

	protected abstract void doOpen();

	protected abstract void doClose();

	protected abstract void doConnect();

	protected abstract void doDisConnect();

	public InetSocketAddress getConnectAddress() {
		return new InetSocketAddress("127.0.0.1", 9876);
	}

}
