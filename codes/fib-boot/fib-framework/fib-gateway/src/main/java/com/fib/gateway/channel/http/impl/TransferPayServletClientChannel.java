package com.fib.gateway.channel.http.impl;

import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.http.HTTPClientChannel;
import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.channel.message.recognizer.impl.SeparatorRecognizer;


public class TransferPayServletClientChannel extends HTTPClientChannel {
	private static final String SID = "SID";

	protected Map createParamters(byte[] requestMessage) {
		Map params = new HashMap();
		// 1. channelId
//		Session session = SessionManager.getSession(requestMessage);
//		if( null == session){
//			// 超时关闭
//			throw new RuntimeException(
//					MultiLanguageResourceBundle
//					.getInstance()
//					.getString(
//							"session.timeout"));
//		}
//		params.put("channelId", session.getSourceChannel().getId());
		String sid = parseSIdFromMessage(requestMessage);
		params.put("_sid", sid);

		// 2.reqeustMessage ：
		// String hex = new String(CodeUtil.BytetoHex(requestMessage));
		// params.put("requestMessage", hex);
		params.put("_msg", new String(requestMessage));

		return params;
	}

	private String parseSIdFromMessage(byte[] message) {
		AbstractMessageRecognizer recognizer = new SeparatorRecognizer();
		Map parameter = new HashMap();
		parameter.put("prefix", "<" + SID + ">");
		parameter.put("suffix", "</" + SID + ">");

		recognizer.setParameters(parameter);
		return recognizer.recognize(message);
	}
}
