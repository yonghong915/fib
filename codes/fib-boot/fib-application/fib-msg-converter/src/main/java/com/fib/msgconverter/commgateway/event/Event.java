/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午07:43:09
 */
package com.fib.msgconverter.commgateway.event;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 网关通讯事件
 * 
 * @author 刘恭亮
 * 
 */
public class Event {
	/**
	 * 事件类型：接收连接失败
	 */
	public static final int EVENT_ACCEPT_ERROR = 7980;

	/**
	 * 事件类型：接收请求报文失败
	 */
	public static final int EVENT_REQUEST_RECEIVE_ERROR = 7981;

	/**
	 * 事件类型：接收应答报文失败
	 */
	public static final int EVENT_RESPONSE_RECEIVE_ERROR = 7982;

	/**
	 * 事件类型：发送请求报文失败
	 */
	public static final int EVENT_REQUEST_SEND_ERROR = 7983;

	/**
	 * 事件类型：发送应答报文失败
	 */
	public static final int EVENT_RESPONSE_SEND_ERROR = 7984;

	/**
	 * 事件类型：连接失败
	 */
	public static final int EVENT_CONNECT_ERROR = 7985;

	/**
	 * 事件类型：内部系统MessageBean请求到达
	 */
	public static final int EVENT_MB_REQUEST_ARRIVED = 9980;

	/**
	 * 事件类型：内部系统MessageBean应答到达
	 */
	public static final int EVENT_MB_RESPONSE_ARRIVED = 9981;

	/**
	 * 事件类型：内部系统MessageBean应答已回送
	 */
	public static final int EVENT_MB_RESPONSE_SENT = 9982;

	/**
	 * 事件类型：内部系统MessageBean请求已发送
	 */
	public static final int EVENT_MB_REQUEST_SENT = 9983;

	/**
	 * 事件类型：普通版：请求报文到达；代理版：外系统请求报文到达
	 */
	public static final int EVENT_REQUEST_ARRIVED = 9990;

	/**
	 * 事件类型：普通版：应答报文到达；代理版：外系统应答报文到达
	 */
	public static final int EVENT_RESPONSE_ARRIVED = 9991;

	/**
	 * 事件类型：普通版：应答报文已回送；代理版：外系统应答报文已回送
	 */
	public static final int EVENT_RESPONSE_SENT = 9992;

	/**
	 * 事件类型：普通版：请求报文已发送；代理版：外系统请求报文已发送
	 */
	public static final int EVENT_REQUEST_SENT = 9993;

	/**
	 * 事件类型：异常
	 */
	public static final int EVENT_EXCEPTION = 9999;

	/**
	 * 事件类型：严重异常，引起通道重启
	 */
	public static final int EVENT_FATAL_EXCEPTION = 10000;

	public static String getEventTypeText(int eventType) {
		switch (eventType) {
		case EVENT_ACCEPT_ERROR:
			// return "Accept Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.accept.error");
		case EVENT_REQUEST_RECEIVE_ERROR:
			// return "Request Receive Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.receiveRequest.error");
		case EVENT_RESPONSE_RECEIVE_ERROR:
			// return "Response Receive Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.receiveResponse.error");
		case EVENT_REQUEST_SEND_ERROR:
			// return "Request Send Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.sendRequest.error");
		case EVENT_RESPONSE_SEND_ERROR:
			// return "Response Send Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.sendResponse.error");
		case EVENT_CONNECT_ERROR:
			// return "Connect Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.connect.error");
		case EVENT_REQUEST_ARRIVED:
			// return "Request Arrived";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.requestArrived");
		case EVENT_RESPONSE_ARRIVED:
			// return "Response Arrived";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.responseArrived");
		case EVENT_RESPONSE_SENT:
			// return "Response Sent";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.responseSent");
		case EVENT_REQUEST_SENT:
			// return "Request Sent";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.requestSent");
		case EVENT_EXCEPTION:
			// return "Unknown Exception";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.unkownException");
		case EVENT_FATAL_EXCEPTION:
			// return "Fatal Error";
			return MultiLanguageResourceBundle.getInstance().getString(
					"Event.getEventTypeText.fatalError");
		default:
			return "" + eventType;
		}
	}

	/**
	 * 事件类型
	 */
	private int eventType;

	/**
	 * 事件产生的渠道
	 */
	private Channel sourceChannel;

	/**
	 * 事件源
	 */
	private Object source;

	/**
	 * 事件相关请求消息
	 */
	private byte[] requestMessage;

	/**
	 * 事件相关应答消息
	 */
	private byte[] responseMessage;

	/**
	 * 可能的异常
	 */
	private Exception excp;

	/**
	 * 事件产生时间
	 */
	private long createTime;

	public Event(int eventType, Channel sourceChannel, Object source,
			Exception excp) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.excp = excp;
		createTime = System.currentTimeMillis();
	}

	public Event(int eventType, Channel sourceChannel, Object source,
			byte[] requestMessage, Exception excp) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.excp = excp;
		createTime = System.currentTimeMillis();
	}

	public Event(int eventType, Channel sourceChannel, Object source,
			byte[] requestMessage, byte[] responseMessage, Exception excp) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
		this.excp = excp;
		createTime = System.currentTimeMillis();
	}

	public Event(int eventType, Channel sourceChannel, Object source,
			byte[] requestMessage, byte[] responseMessage) {
		super();
		this.eventType = eventType;
		this.sourceChannel = sourceChannel;
		this.source = source;
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
		createTime = System.currentTimeMillis();
	}

	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss SSS");

	public String toString() {
		String NEW_LINE = System.getProperty("line.separator");
		StringBuffer buf = new StringBuffer(10240);
		buf.append(NEW_LINE);
		// buf.append("------------------ Event At  ");
		// buf.append(format.format(new Date(createTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Event.toString.eventAt",
				new String[] { format.format(new Date(createTime)) }));

		buf.append(NEW_LINE);
		// buf.append("eventType = ");
		// buf.append(Event.getEventTypeText(getEventType()));
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Event.toString.eventType",
				new String[] { Event.getEventTypeText(getEventType()) }));

		buf.append(NEW_LINE);

		if (getSourceChannel() != null) {
			// buf.append("sourceChannel = ");
			// buf.append(getSourceChannel().toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Event.toString.sourceChannel",
					new String[] { getSourceChannel().toString() }));

			buf.append(NEW_LINE);
		}

		if (getSource() != null) {
			// buf.append("source = ");
			// buf.append(getSource().toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Event.toString.source",
					new String[] { getSource().toString() }));

			buf.append(NEW_LINE);
		}

		if (getRequestMessage() != null) {
			// buf.append("requestMessage = \n");
			// buf.append(CodeUtil.Bytes2FormattedText(getRequestMessage()));
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Event.toString.requestMessage",
					new String[] { CodeUtil
							.Bytes2FormattedText(getRequestMessage()) }));

			buf.append(NEW_LINE);
		}

		if (getResponseMessage() != null) {
			// buf.append("responseMessage = \n");
			// buf.append(CodeUtil.Bytes2FormattedText(getResponseMessage()));
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Event.toString.responseMessage",
					new String[] { CodeUtil
							.Bytes2FormattedText(getResponseMessage()) }));

			buf.append(NEW_LINE);
		}

		if (getExcp() != null) {
			// buf.append("exception = ");
			// buf.append(ExceptionUtil.getExceptionDetail(getExcp()));
			buf.append(MultiLanguageResourceBundle.getInstance()
					.getString(
							"Event.toString.exception",
							new String[] { ExceptionUtil
									.getExceptionDetail(getExcp()) }));

			buf.append(NEW_LINE);
		}

		buf
				.append("-----------------------------------------------------------------------");
		buf.append(NEW_LINE);

		return buf.toString();
	}

	/**
	 * @return the eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the sourceChannel
	 */
	public Channel getSourceChannel() {
		return sourceChannel;
	}

	/**
	 * @param sourceChannel
	 *            the sourceChannel to set
	 */
	public void setSourceChannel(Channel sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @return the excp
	 */
	public Exception getExcp() {
		return excp;
	}

	/**
	 * @param excp
	 *            the excp to set
	 */
	public void setExcp(Exception excp) {
		this.excp = excp;
	}

	/**
	 * @return the requestMessage
	 */
	public byte[] getRequestMessage() {
		return requestMessage;
	}

	/**
	 * @param requestMessage
	 *            the requestMessage to set
	 */
	public void setRequestMessage(byte[] requestMessage) {
		this.requestMessage = requestMessage;
	}

	/**
	 * @return the responseMessage
	 */
	public byte[] getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(byte[] responseMessage) {
		this.responseMessage = responseMessage;
	}

}
