package com.fib.core.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fib.commons.util.CommUtils;

import cn.hutool.core.convert.Convert;

public enum ThreadLocalMessageContext {
	INSTANCE;

	private static final ThreadLocal<MessageContext> THREAD_CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * 设置本线程上下文环境
	 */
	private void setMessageContext(MessageContext context) {
		THREAD_CONTEXT.set(context);
	}

	/**
	 * 得到本线程上下文环境
	 */
	private MessageContext getMessageContext() {
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
		CommUtils.isNull(value, k -> ThreadLocalMessageContext.INSTANCE.getMessageContext().setProperty(key, value));
	}

	public String get(String key) {
		return get(key, String.class);
	}

	public <T> T get(String key, Class<T> clazz) {
		Object obj = ThreadLocalMessageContext.INSTANCE.getMessageContext().getProperty(key);
		return Convert.convert(clazz, obj);
	}
}