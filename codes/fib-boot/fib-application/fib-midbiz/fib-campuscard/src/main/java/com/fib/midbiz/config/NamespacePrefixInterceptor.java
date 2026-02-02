package com.fib.midbiz.config;

import java.io.OutputStream;

import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class NamespacePrefixInterceptor extends AbstractPhaseInterceptor<Message> {

	public NamespacePrefixInterceptor() {
		// 在发送消息之前的阶段进行拦截
		super(Phase.PRE_STREAM);
		// 添加依赖的拦截器
		addAfter(AttachmentOutInterceptor.class.getName());
	}

	@Override
	public void handleMessage(Message message) {
		try {
			// 获取输出流
			OutputStream outputStream = message.getContent(OutputStream.class);
			if (outputStream == null) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
