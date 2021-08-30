/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 下午03:27:37
 */
package com.fib.msgconverter.commgateway.channel.nio.messagebean;

import com.fib.msgconverter.commgateway.channel.nio.SocketServerChannel;
import com.fib.msgconverter.commgateway.event.Event;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;

/**
 * MessageBean服务器通道。专门负责接收MessageBean请求，直接打包成目的系统的报文发送到目的系统
 * 
 * @author 刘恭亮
 * 
 */
public class MessageBeanServerChannel extends SocketServerChannel {
	private static final int LEN_FIELD_LEN = 8;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.nio.SocketServerChannel#sendResponseMessage(byte[])
	 */
	public void sendResponseMessage(byte[] responseMessage) {
		Session session = SessionManager.getSession(responseMessage);
		if( null == session){
			// 超时关闭
			return;
		}
		// 加上长度域
		byte[] mbMessage = new byte[responseMessage.length + LEN_FIELD_LEN];
		String len = Integer.toString(responseMessage.length);
		if (len.length() < LEN_FIELD_LEN) {
			for (int i = len.length(); i < LEN_FIELD_LEN; i++) {
				len = "0" + len;
			}
		}
		byte[] bLen = len.getBytes();
		System.arraycopy(bLen, 0, mbMessage, 0, LEN_FIELD_LEN);
		System.arraycopy(responseMessage, 0, mbMessage, LEN_FIELD_LEN,
				responseMessage.length);
		// 重新关联Session
		SessionManager.attachSession(mbMessage, session);
		super.sendResponseMessage(mbMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.Channel#onRequestMessageArrived(java.lang.Object,
	 *      byte[])
	 */
	protected void onRequestMessageArrived(Object source, byte[] requestMessage) {
		// 1. 取报文XML部分：去掉头8个字节报文长度域
		byte[] mbXml = new byte[requestMessage.length - LEN_FIELD_LEN];
		System.arraycopy(requestMessage, LEN_FIELD_LEN, mbXml, 0, mbXml.length);

		// 2. 产生事件
		Event e = new Event(Event.EVENT_MB_REQUEST_ARRIVED, this, source,
				mbXml, (byte[]) null);
		eventQueue.postEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.channel.Channel#onResponseMessageSent(java.lang.Object,
	 *      byte[])
	 */
	protected void onResponseMessageSent(Object source, byte[] responseMessage) {
		Event e = new Event(Event.EVENT_MB_RESPONSE_SENT, this, source,
				responseMessage, (byte[]) null);
		eventQueue.postEvent(e);
	}

}
