package com.fib.netty.vo;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Request {
	protected static final Map<Long, Request> futures = new ConcurrentHashMap<>();
	private static final AtomicLong aid = new AtomicLong(0);
	private long id;
	private Object requestMsg;
	private Object result;
	private long timeout = 5000;

	public Request() {
		id = aid.incrementAndGet();
		addFuture(this);
	}

	public static void addFuture(Request future) {
		futures.put(future.getId(), future);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Object getRequestMsg() {
		return requestMsg;
	}

	public void setRequestMsg(Object requestMsg) {
		this.requestMsg = requestMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",");
		sj.add(String.valueOf(this.id)).add(String.valueOf(this.requestMsg)).add(String.valueOf(this.result))
				.add(String.valueOf(this.timeout));
		return sj.toString();
	}
}
