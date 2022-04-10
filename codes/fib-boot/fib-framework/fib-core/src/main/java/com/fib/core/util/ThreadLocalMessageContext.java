package com.fib.core.util;

import java.util.function.Consumer;

public enum ThreadLocalMessageContext {
	INSTANCE;

	private static final ThreadLocal<MessageContext> THREAD_CONTEXT = new ThreadLocal<>();

	/**
	 * 设置本线程上下文环境
	 */
	void setMessageContext(MessageContext context) {
		THREAD_CONTEXT.set(context);
	}

	/**
	 * 得到本线程上下文环境
	 */
	MessageContext getMessageContext() {
		MessageContext ctx = THREAD_CONTEXT.get();
		if (ctx == null) {
			ctx = new MessageContext();
			setMessageContext(ctx);
		}
		return ctx;
	}

	/**
	 * 移除程上下文环境
	 */
	void removeContext() {
		THREAD_CONTEXT.remove();
	}

	public void set(String key, Object value) {
		isNull(value, k -> ThreadLocalMessageContext.INSTANCE.getMessageContext().setProperty(key, value));
	}

	public Object get(String key) {
		return ThreadLocalMessageContext.INSTANCE.getMessageContext().getProperty(key);
	}

	public void isNull(Object value, Consumer<Object> errorSupplier) {
		if (null != value) {
			errorSupplier.accept(value);
		}
	}
}