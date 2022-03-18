package com.fib.netty.vo;

import java.util.StringJoiner;

public class Response {
	private long id;
	private Object result;

	public Response() {
	}

	public Response(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",");
		sj.add(String.valueOf(this.id)).add(String.valueOf(this.result));
		return sj.toString();
	}
}
