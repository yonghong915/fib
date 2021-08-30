package com.fib.msgconverter.commgateway.channel.http.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.http.HTTPClientChannel;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

public class BosentServletClientChannel extends HTTPClientChannel {
	protected Map<String, String> createParamters(byte[] requestMessage) {
		Map<String, String> params = new HashMap<>();
		// 1. channelId
		Session session = SessionManager.getSession(requestMessage);
		if (null == session) {
			// 超时关闭
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("session.timeout"));
		}
		params.put("channelId", session.getSourceChannel().getId());

		// 2.reqeustMessage ：原始报文转换成字符串
		if (null == config.getContentCharset()) {
			params.put("requestMessage", new String(requestMessage));
		} else {
			try {
				params.put("requestMessage", new String(requestMessage, config.getContentCharset()));
			} catch (UnsupportedEncodingException e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
		}
		return params;
	}
}