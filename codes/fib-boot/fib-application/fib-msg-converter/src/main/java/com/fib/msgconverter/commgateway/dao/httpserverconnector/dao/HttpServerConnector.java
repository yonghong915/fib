package com.fib.msgconverter.commgateway.dao.httpserverconnector.dao;

import java.math.BigDecimal;

public class HttpServerConnector{
	public HttpServerConnector() {

	}

	//id
	private String id;

	//服务端口
	private int port;

	//超时时间
	private int timeout;

	//最大缓冲区大小
	private int socketBufferSize;

	//是否检测弱连接
	private String staleConnectionCheck;

	//是否启用Nagle算法以优化传输效率
	private String tcpNodelay;

	//Http元素编码格式
	private String elementCharset;

	//Http传输内容编码格式
	private String contentCharset;

	//最大并发处理数量
	private int backlog;

	//校验器类名
	private String verifierClassName;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setPort(int newPort) {
		this.port = newPort;
	}

	public void setTimeout(int newTimeout) {
		this.timeout = newTimeout;
	}

	public void setSocketBufferSize(int newSocketBufferSize) {
		this.socketBufferSize = newSocketBufferSize;
	}

	public void setStaleConnectionCheck(String newStaleConnectionCheck) {
		this.staleConnectionCheck = newStaleConnectionCheck;
	}

	public void setTcpNodelay(String newTcpNodelay) {
		this.tcpNodelay = newTcpNodelay;
	}

	public void setElementCharset(String newElementCharset) {
		this.elementCharset = newElementCharset;
	}

	public void setContentCharset(String newContentCharset) {
		this.contentCharset = newContentCharset;
	}

	public void setBacklog(int newBacklog) {
		this.backlog = newBacklog;
	}

	public void setVerifierClassName(String newVerifierClassName) {
		this.verifierClassName = newVerifierClassName;
	}

	public String getId() {
		return this.id;
	}

	public int getPort() {
		return this.port;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getSocketBufferSize() {
		return this.socketBufferSize;
	}

	public String getStaleConnectionCheck() {
		return this.staleConnectionCheck;
	}

	public String getTcpNodelay() {
		return this.tcpNodelay;
	}

	public String getElementCharset() {
		return this.elementCharset;
	}

	public String getContentCharset() {
		return this.contentCharset;
	}

	public int getBacklog() {
		return this.backlog;
	}

	public String getVerifierClassName() {
		return this.verifierClassName;
	}

}