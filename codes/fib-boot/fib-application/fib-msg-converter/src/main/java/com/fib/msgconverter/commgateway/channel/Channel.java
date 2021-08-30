/**
 * 北京长信通信息技术有限公司
 * 2008-8-21 下午04:27:30
 */
package com.fib.msgconverter.commgateway.channel;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.event.Event;
import com.fib.msgconverter.commgateway.event.EventHandler;
import com.fib.msgconverter.commgateway.event.EventQueue;
import com.fib.msgconverter.commgateway.module.impl.ExceptionMonitor;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 通讯通道
 * 
 * @author 刘恭亮
 */
public abstract class Channel {
	// 通道类型-SOCKET短连接客户端
	public static final String CHANNEL_TYP_SOCKET_CLIENT = "SOCKET_CLIENT";
	// 通道类型-SOCKET短连接服务端
	public static final String CHANNEL_TYP_SOCKET_SERVER = "SOCKET_SERVER";
	// 通道类型-长连接
	public static final String CHANNEL_TYP_LONG_CONN = "LONG_SOCKET";
	// 通道类型-MQ
	public static final String CHANNEL_TYP_MQ = "MQ";
	// 通道类型-HTTP客户端
	public static final String CHANNEL_TYP_HTTP_CLIENT = "HTTP_CLIENT";
	// 通道类型-HTTP服务端
	public static final String CHANNEL_TYP_HTTP_SERVER = "HTTP_SERVER";

	/**
	 * 通道主配置：id、name、startup、deploy等
	 */
	protected ChannelMainConfig mainConfig;

	/**
	 * 通道具体配置
	 */
	protected ChannelConfig channelConfig;

	/**
	 * 日志记录器
	 */
	protected Logger logger;

	/**
	 * 报文类型识别器
	 */
	protected AbstractMessageRecognizer messageTypeRecognizer;

	/**
	 * 应答报文返回码识别器
	 */
	protected AbstractMessageRecognizer returnCodeRecognizer;

	/**
	 * 事件处理器
	 */
	protected EventHandler eventHandler;

	/**
	 * 事件队列
	 */
	protected EventQueue eventQueue;

	/*
	 * channle类型
	 */
	// add by jiayx
	// 编写的定制类在某些情况下不按照其channel类型（如MQ的定制channel继承MQChannel）继承而仅继承Channel，
	// 在此种情况下如仅得到channel(如开发平台加载channel时)就不能知道connector的类型，
	// 因此加入channelType标注Channel类型
	protected String channelType;

	public String toString() {
		// StringBuffer buf = new StringBuffer(128);
		// buf.append(mainConfig.toString());
		// buf.append(" type[");
		// buf.append(channelConfig.getType());
		// buf.append("] class[");
		// buf.append(channelConfig.getClassName());
		// buf.append("]");
		// return buf.toString();
		return MultiLanguageResourceBundle.getInstance().getString(
				"Channel.toString",
				new String[] { mainConfig.toString(), channelConfig.getType(),
						channelConfig.getClassName() });
	}

	/**
	 * 加载配置
	 * 
	 * @param configFileName
	 */
	public abstract void loadConfig(InputStream in);

	/**
	 * 启动通道
	 */
	public abstract void start();

	/**
	 * 停止通道
	 */
	public abstract void shutdown();

	/**
	 * 重启通道
	 */
	public void restart() {
		shutdown();
		start();
	}

	public Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger(mainConfig.getId());
		}
		return logger;
	}

	/**
	 * 当接收连接异常
	 * 
	 * @param source
	 * @param excp
	 */
	protected void onAcceptException(Object source, Exception excp) {
		Event e = new Event(Event.EVENT_ACCEPT_ERROR, this, source, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当接收请求消息异常
	 * 
	 * @param source
	 * @param excp
	 */
	protected void onRequestMessageReceiveException(Object source,
			Exception excp) {
		Event e = new Event(Event.EVENT_REQUEST_RECEIVE_ERROR, this, source,
				excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当请求消息到达。通道为服务器端
	 * 
	 * @param source
	 * @param requestMessage
	 */
	protected void onRequestMessageArrived(Object source, byte[] requestMessage) {
		// 产生请求到达事件
		Event e = new Event(Event.EVENT_REQUEST_ARRIVED, this, source,
				requestMessage, (byte[]) null);
		eventQueue.postEvent(e);
	}

	/**
	 * 当建立连接异常
	 * 
	 * @param source
	 * @param requestMessage
	 * @param excp
	 */
	protected void onConnectException(Object source, byte[] requestMessage,
			Exception excp) {
		Event e = new Event(Event.EVENT_CONNECT_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当建立连接异常, 加入异常处理平台
	 * 
	 */
	protected void onConnectException(Object source, byte[] requestMessage,
			Exception excp, String transcationId, String ip, int port) {

		Event e = new Event(Event.EVENT_CONNECT_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, ip, port, requestMessage, null,
				excp);

	}

	/**
	 * 当请求消息发送异常
	 * 
	 * @param source
	 * @param requestMessage
	 * @param excp
	 */
	protected void onRequestMessageSendException(Object source,
			byte[] requestMessage, Exception excp) {
		Event e = new Event(Event.EVENT_REQUEST_SEND_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当请求消息发送异常, 加入异常处理平台
	 */
	protected void onRequestMessageSendException(Object source,
			byte[] requestMessage, Exception excp, String transcationId,
			String ip, int port) {
		Event e = new Event(Event.EVENT_REQUEST_SEND_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, ip, port, requestMessage, null,
				excp);
	}

	/**
	 * 当请求消息发送完毕。通道为客户端
	 * 
	 * @param source
	 * @param requestMessage
	 */
	protected void onRequestMessageSent(Object source, byte[] requestMessage) {
		// 产生请求到达事件
		Event e = new Event(Event.EVENT_REQUEST_SENT, this, source,
				requestMessage, (byte[]) null);
		eventQueue.postEvent(e);
	}

	/**
	 * 当应答消息接收异常
	 * 
	 * @param source
	 * @param requestMessage
	 * @param excp
	 */
	protected void onResponseMessageReceiveException(Object source,
			byte[] requestMessage, Exception excp) {
		Event e = new Event(Event.EVENT_RESPONSE_RECEIVE_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);

	}

	/**
	 * 当应答消息接收异常, 加入异常处理平台
	 * 
	 * @param source
	 * @param requestMessage
	 * @param excp
	 */
	protected void onResponseMessageReceiveException(Object source,
			byte[] requestMessage, Exception excp, String transcationId,
			String ip, int port) {
		Event e = new Event(Event.EVENT_RESPONSE_RECEIVE_ERROR, this, source,
				requestMessage, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, ip, port, requestMessage, null,
				excp);
	}

	/**
	 * 当应答消息到达：通道为客户端，发送请求消息并同步接收到应答后产生此事件
	 * 
	 * @param source
	 * @param responseMessage
	 */
	protected void onResponseMessageArrived(Object source,
			byte[] requestMessage, byte[] responseMessage) {
		// 产生应答到达事件
		Event e = new Event(Event.EVENT_RESPONSE_ARRIVED, this, source,
				requestMessage, responseMessage);
		eventQueue.postEvent(e);
	}

	/**
	 * 当应答消息发送异常
	 * 
	 * @param source
	 * @param responseMessage
	 * @param excp
	 */
	protected void onResponseMessageSendException(Object source,
			byte[] responseMessage, Exception excp) {
		Event e = new Event(Event.EVENT_RESPONSE_SEND_ERROR, this, source,
				null, responseMessage, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当应答消息发送异常, 加入异常处理平台
	 */
	protected void onResponseMessageSendException(Object source,
			byte[] responseMessage, Exception excp, String transcationId,
			String ip, int port) {
		Event e = new Event(Event.EVENT_RESPONSE_SEND_ERROR, this, source,
				null, responseMessage, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, ip, port, null, responseMessage,
				excp);
	}

	/**
	 * 当应答消息到达发送完毕
	 * 
	 * @param source
	 * @param responseMessage
	 */
	protected void onResponseMessageSent(Object source, byte[] responseMessage) {
		// 产生应答到达事件
		Event e = new Event(Event.EVENT_RESPONSE_SENT, this, source, null,
				responseMessage);
		eventQueue.postEvent(e);
	}

	/**
	 * 当发生未定类型异常
	 * 
	 * @param source
	 * @param excp
	 */
	protected void onException(Object source, Exception excp) {
		Event e = new Event(Event.EVENT_EXCEPTION, this, source, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当发生未定类型异常, 加入异常处理平台
	 * 
	 */
	protected void onException(Object source, Exception excp,
			String transcationId) {
		Event e = new Event(Event.EVENT_EXCEPTION, this, source, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, excp);
	}

	/**
	 * 当发生严重异常
	 * 
	 * @param source
	 * @param excp
	 */
	protected void onFatalException(Object source, Exception excp) {
		Event e = new Event(Event.EVENT_FATAL_EXCEPTION, this, source, excp);
		eventQueue.postEvent(e);
	}

	/**
	 * 当发生严重异常,加入异常处理平台
	 */
	protected void onFatalException(Object source, Exception excp,
			String transcationId) {
		Event e = new Event(Event.EVENT_FATAL_EXCEPTION, this, source, excp);
		eventQueue.postEvent(e);
		ExceptionMonitor.dump(transcationId, excp);
	}

	/**
	 * 发送请求报文
	 * 
	 * @param requestMessage
	 */
	public abstract void sendRequestMessage(byte[] requestMessage,
			boolean isASync, int timeout);

	/**
	 * 发送应答报文
	 * 
	 * @param responseMessage
	 */
	public abstract void sendResponseMessage(byte[] responseMessage);

	/**
	 * 关闭通讯源。通讯源可能是一个Socket、SocketChannel，或其他请求报文接收源。
	 * 
	 * @param source
	 */
	public abstract void closeSource(Object source);

	/**
	 * @return the eventQueue
	 */
	public EventQueue getEventQueue() {
		return eventQueue;
	}

	/**
	 * @param eventQueue
	 *            the eventQueue to set
	 */
	public void setEventQueue(EventQueue eventQueue) {
		this.eventQueue = eventQueue;
	}

	public AbstractMessageRecognizer getReturnCodeRecognizer() {
		return returnCodeRecognizer;
	}

	public void setReturnCodeRecognizer(
			AbstractMessageRecognizer returnCodeRecognizer) {
		this.returnCodeRecognizer = returnCodeRecognizer;
	}

	public String getId() {
		return mainConfig.getId();
	}

	/**
	 * @return the mainConfig
	 */
	public ChannelMainConfig getMainConfig() {
		return mainConfig;
	}

	/**
	 * @param mainConfig
	 *            the mainConfig to set
	 */
	public void setMainConfig(ChannelMainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	/**
	 * @return the channelConfig
	 */
	public ChannelConfig getChannelConfig() {
		return channelConfig;
	}

	/**
	 * @param channelConfig
	 *            the channelConfig to set
	 */
	public void setChannelConfig(ChannelConfig channelConfig) {
		this.channelConfig = channelConfig;
	}

	/**
	 * @return the messageTypeRecognizer
	 */
	public AbstractMessageRecognizer getMessageTypeRecognizer() {
		return messageTypeRecognizer;
	}

	/**
	 * @param messageTypeRecognizer
	 *            the messageTypeRecognizer to set
	 */
	public void setMessageTypeRecognizer(
			AbstractMessageRecognizer messageTypeRecognizer) {
		this.messageTypeRecognizer = messageTypeRecognizer;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

}
