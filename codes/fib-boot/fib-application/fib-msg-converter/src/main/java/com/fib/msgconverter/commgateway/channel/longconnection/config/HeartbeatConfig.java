package com.fib.msgconverter.commgateway.channel.longconnection.config;

/**
 * 长连接心跳包配置。
 * 
 * 心跳包是在长连接空闲一段时间后发送或接收的一种探测报文，用来验证连接是否可用。
 * 
 * @author 刘恭亮
 * 
 */
public class HeartbeatConfig {
	/**
	 * 心跳包所属连接的id
	 */
	private String connectionId;

	/**
	 * 通讯方向：send/receive
	 */
	private int direction;

	/**
	 * 发送间隔，单位毫秒：通讯方向为send时有效
	 */
	private long interval;

	/**
	 * 心跳包应答所属连接的id：当存在心跳包应答报文时有效，默认是connectionId
	 */
	private String responseConnectionId;

	/**
	 * 心跳包报文符号
	 */
	private String messageSymbolId;

	private MessageSymbol messageSymbol;

	/**
	 * 心跳包应答报文符号：当需要心跳包应答报文时配置
	 */
	private String responseMessageSymbolId;

	private MessageSymbol responseMessageSymbol;

	/**
	 * 心跳包应答原路返回标志，仅当该心跳包为发送心跳包时有效
	 */
	private boolean responseTurnBack = false;

	/**
	 * @return the connectionId
	 */
	public String getConnectionId() {
		return connectionId;
	}

	/**
	 * @param connectionId the connectionId to set
	 */
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the interval
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * @return the responseConnectionId
	 */
	public String getResponseConnectionId() {
		return responseConnectionId;
	}

	/**
	 * @param responseConnectionId the responseConnectionId to set
	 */
	public void setResponseConnectionId(String responseConnectionId) {
		this.responseConnectionId = responseConnectionId;
	}

	public String getMessageSymbolId() {
		return messageSymbolId;
	}

	public void setMessageSymbolId(String messageSymbol) {
		messageSymbolId = messageSymbol;
	}

	public String getResponseMessageSymbolId() {
		return responseMessageSymbolId;
	}

	public void setResponseMessageSymbolId(String responseMessageSymbol) {
		responseMessageSymbolId = responseMessageSymbol;
	}

	public MessageSymbol getMessageSymbol() {
		return messageSymbol;
	}

	public void setMessageSymbol(MessageSymbol messageSymbol) {
		this.messageSymbol = messageSymbol;
	}

	public MessageSymbol getResponseMessageSymbol() {
		return responseMessageSymbol;
	}

	public void setResponseMessageSymbol(MessageSymbol responseMessageSymbol) {
		this.responseMessageSymbol = responseMessageSymbol;
	}

	public boolean isResponseTurnBack() {
		return responseTurnBack;
	}

	public void setResponseTurnBack(boolean responseTurnBack) {
		this.responseTurnBack = responseTurnBack;
	}

}
