package com.fib.gateway.channel.http.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.http.HTTPClientChannel;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.session.Session;
import com.fib.gateway.session.SessionManager;


public class BosentServletClientChannel extends HTTPClientChannel {
	protected Map createParamters(byte[] requestMessage) {
		Map params = new HashMap();
		// 1. channelId
		//Session session = SessionManager.getSession(requestMessage);
//		if( null == session){
//			// 超时关闭
//			throw new RuntimeException(
//					MultiLanguageResourceBundle
//					.getInstance()
//					.getString(
//							"session.timeout"));
//		}
		//params.put("channelId", session.getSourceChannel().getId());

		// 2.reqeustMessage ：原始报文转换成字符串
		if (null == config.getContentCharset()) {
			params.put("requestMessage", new String(requestMessage));
		} else {
			try {
				params.put("requestMessage", new String(requestMessage, config
						.getContentCharset()));
			} catch (UnsupportedEncodingException e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
		}

		return params;
	}
}
