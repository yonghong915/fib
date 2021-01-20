
package com.fib.gateway.channel.http.impl;

import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.http.HTTPClientChannel;
import com.fib.gateway.message.util.CodeUtil;


/**
 * 消息Bean服务客户端通道
 * 
 * 
 * 
 */
public class MessageBeanServletClientChannel extends HTTPClientChannel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.giantstone.commgateway.channel.http.HTTPClientChannel#createParamters
	 * (byte[])
	 */
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
//		// 2. EXTERNALSERIALNUM : 外部流水号,暂时使用sessionId代替
//		params.put(SessionConstants.EXTERNAL_SERIAL_NUM, null == session
//				.getId() ? "" : session.getId());

		// 2.reqeustMessage ：原始报文转换为16进制字符串
		String hex = new String(CodeUtil.BytetoHex(requestMessage));
		params.put("requestMessage", hex);

		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.giantstone.commgateway.channel.http.HTTPClientChannel#parseResponseBody
	 * (byte[])
	 */
	protected byte[] parseResponseBody(byte[] responseBody) {
		// 16进制字符串报文转换为二进制报文
		return CodeUtil.HextoByte(responseBody);
	}

}
