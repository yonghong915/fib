package com.fib.autoconfigure.msgconverter.channel.event;

/**
 * 事件类型
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 11:14:40
 */
public enum EventType {

	/** 7980, ACCEPT_ERROR, 接收连接失败 */
	ACCEPT_ERROR(7980, "ACCEPT_ERROR", "接收连接失败"),

	/** 7981, REQUEST_RECEIVE_ERROR, 接收请求报文失败 */
	REQUEST_RECEIVE_ERROR(7981, "REQUEST_RECEIVE_ERROR", "接收请求报文失败"),

	/** 7982, RESPONSE_RECEIVE_ERROR, 接收应答报文失败 */
	RESPONSE_RECEIVE_ERROR(7982, "RESPONSE_RECEIVE_ERROR", "接收应答报文失败"),

	/** 7983, REQUEST_SEND_ERROR, 发送请求报文失败 */
	REQUEST_SEND_ERROR(7983, "REQUEST_SEND_ERROR", "发送请求报文失败"),

	/** 7984, RESPONSE_SEND_ERROR, 发送应答报文失败 */
	RESPONSE_SEND_ERROR(7984, "RESPONSE_SEND_ERROR", "发送应答报文失败"),

	/** 7985, CONNECT_ERROR, 连接失败 */
	CONNECT_ERROR(7985, "CONNECT_ERROR", "连接失败"),

	/** 9980, MB_REQUEST_ARRIVED, 内部系统MessageBean请求到达 */
	MB_REQUEST_ARRIVED(9980, "MB_REQUEST_ARRIVED", "内部系统MessageBean请求到达"),

	/** 9981, MB_RESPONSE_ARRIVED, 内部系统MessageBean应答到达 */
	MB_RESPONSE_ARRIVED(9981, "MB_RESPONSE_ARRIVED", "内部系统MessageBean应答到达"),

	/** 9982, MB_RESPONSE_SENT, 内部系统MessageBean请求已发送 */
	MB_RESPONSE_SENT(9982, "MB_RESPONSE_SENT", "内部系统MessageBean请求已发送"),

	/** 9983, MB_REQUEST_SENT, 内部系统MessageBean请求已发送 */
	MB_REQUEST_SENT(9983, "MB_REQUEST_SENT", "内部系统MessageBean请求已发送"),

	/** 9990, REQUEST_ARRIVED,普通版：请求报文到达；代理版：外系统请求报文到达 */
	REQUEST_ARRIVED(9990, "REQUEST_ARRIVED", "普通版：请求报文到达；代理版：外系统请求报文到达"),

	/** 9991, RESPONSE_ARRIVED, 普通版：应答报文到达；代理版：外系统应答报文到达 */
	RESPONSE_ARRIVED(9991, "RESPONSE_ARRIVED", "普通版：应答报文到达；代理版：外系统应答报文到达"),

	/** 9992, RESPONSE_SENT, 普通版：应答报文已回送；代理版：外系统应答报文已回送 */
	RESPONSE_SENT(9992, "RESPONSE_SENT", "普通版：应答报文已回送；代理版：外系统应答报文已回送"),

	/** 9993, REQUEST_SENT, 普通版：请求报文已发送；代理版：外系统请求报文已发送 */
	REQUEST_SENT(9993, "REQUEST_SENT", "普通版：请求报文已发送；代理版：外系统请求报文已发送"),

	/** 9999, EXCEPTION, 异常 */
	EXCEPTION(9999, "EXCEPTION", "异常"),

	/** 10000, FATAL_EXCEPTION, 严重异常，引起通道重启 */
	FATAL_EXCEPTION(10000, "FATAL_EXCEPTION", "严重异常，引起通道重启");

	private int code;
	private String name;
	private String text;

	EventType(int code, String name, String text) {
		this.code = code;
		this.name = name;
		this.text = text;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}
}
