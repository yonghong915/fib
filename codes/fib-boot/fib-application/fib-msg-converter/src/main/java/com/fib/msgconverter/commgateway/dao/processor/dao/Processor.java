package com.fib.msgconverter.commgateway.dao.processor.dao;

import java.math.BigDecimal;

public class Processor{
	public Processor() {

	}

	//id
	private String id;

	//处理器类型
	private String processorType;

	//是否本地发起的
	private String isLocal;

	//路由规则ID
	private String routeRuleId;

	//源通道是否是异步处理
	private String sourceAsync;

	//目的通道是否是异步处理
	private String destAsync;

	//源通道通讯对象类型
	private String sourceChannelMessageObjectType;

	//源通道Map编码格式
	private String sourceMapCharset;

	//目的通道数据对象类型
	private String destChannelMessageObjectType;

	//目的通道Map编码格式
	private String destMapCharset;

	//处理器超时时间
	private int timeout;

	//请求报文转换器ID
	private String requestMessageTransformerId;

	//请求报文处理类
	private String requestMessageHandlerClass;

	//应答报文转换器ID
	private String responseMessageTransformerId;

	//应答报文处理类
	private String responseMessageHandlerClass;

	//错误应答报文转换器ID
	private String errorMessageTransformerId;

	//错误报文处理类
	private String errorMessageHandlerClass;

	//内部错误报文转换器ID
	private String internalErrorMessageTransformerId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setProcessorType(String newProcessorType) {
		this.processorType = newProcessorType;
	}

	public void setIsLocal(String newIsLocal) {
		this.isLocal = newIsLocal;
	}

	public void setRouteRuleId(String newRouteRuleId) {
		this.routeRuleId = newRouteRuleId;
	}

	public void setSourceAsync(String newSourceAsync) {
		this.sourceAsync = newSourceAsync;
	}

	public void setDestAsync(String newDestAsync) {
		this.destAsync = newDestAsync;
	}

	public void setSourceChannelMessageObjectType(String newSourceChannelMessageObjectType) {
		this.sourceChannelMessageObjectType = newSourceChannelMessageObjectType;
	}

	public void setSourceMapCharset(String newSourceMapCharset) {
		this.sourceMapCharset = newSourceMapCharset;
	}

	public void setDestChannelMessageObjectType(String newDestChannelMessageObjectType) {
		this.destChannelMessageObjectType = newDestChannelMessageObjectType;
	}

	public void setDestMapCharset(String newDestMapCharset) {
		this.destMapCharset = newDestMapCharset;
	}

	public void setTimeout(int newTimeout) {
		this.timeout = newTimeout;
	}

	public void setRequestMessageTransformerId(String newRequestMessageTransformerId) {
		this.requestMessageTransformerId = newRequestMessageTransformerId;
	}

	public void setRequestMessageHandlerClass(String newRequestMessageHandlerClass) {
		this.requestMessageHandlerClass = newRequestMessageHandlerClass;
	}

	public void setResponseMessageTransformerId(String newResponseMessageTransformerId) {
		this.responseMessageTransformerId = newResponseMessageTransformerId;
	}

	public void setResponseMessageHandlerClass(String newResponseMessageHandlerClass) {
		this.responseMessageHandlerClass = newResponseMessageHandlerClass;
	}

	public void setErrorMessageTransformerId(String newErrorMessageTransformerId) {
		this.errorMessageTransformerId = newErrorMessageTransformerId;
	}

	public void setErrorMessageHandlerClass(String newErrorMessageHandlerClass) {
		this.errorMessageHandlerClass = newErrorMessageHandlerClass;
	}

	public void setInternalErrorMessageTransformerId(String newInternalErrorMessageTransformerId) {
		this.internalErrorMessageTransformerId = newInternalErrorMessageTransformerId;
	}

	public String getId() {
		return this.id;
	}

	public String getProcessorType() {
		return this.processorType;
	}

	public String getIsLocal() {
		return this.isLocal;
	}

	public String getRouteRuleId() {
		return this.routeRuleId;
	}

	public String getSourceAsync() {
		return this.sourceAsync;
	}

	public String getDestAsync() {
		return this.destAsync;
	}

	public String getSourceChannelMessageObjectType() {
		return this.sourceChannelMessageObjectType;
	}

	public String getSourceMapCharset() {
		return this.sourceMapCharset;
	}

	public String getDestChannelMessageObjectType() {
		return this.destChannelMessageObjectType;
	}

	public String getDestMapCharset() {
		return this.destMapCharset;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public String getRequestMessageTransformerId() {
		return this.requestMessageTransformerId;
	}

	public String getRequestMessageHandlerClass() {
		return this.requestMessageHandlerClass;
	}

	public String getResponseMessageTransformerId() {
		return this.responseMessageTransformerId;
	}

	public String getResponseMessageHandlerClass() {
		return this.responseMessageHandlerClass;
	}

	public String getErrorMessageTransformerId() {
		return this.errorMessageTransformerId;
	}

	public String getErrorMessageHandlerClass() {
		return this.errorMessageHandlerClass;
	}

	public String getInternalErrorMessageTransformerId() {
		return this.internalErrorMessageTransformerId;
	}

}