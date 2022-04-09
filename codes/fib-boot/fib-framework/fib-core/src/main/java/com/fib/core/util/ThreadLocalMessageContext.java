package com.fib.core.util;

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
	public MessageContext getMessageContext() {
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
	public void removeContext() {
		THREAD_CONTEXT.remove();
	}
}
