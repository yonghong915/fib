package com.fib.msgconverter.commgateway.channel.config.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.processor.event.ActionConfig;

/**
 * 报文处理器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-31
 */
public class Processor {
	private String id;
	private int type = 153;
	private String routeRuleId;
	private MessageTransformerConfig requestMessageConfig;
	private MessageHandlerConfig requestHandlerConfig;
	private MessageTransformerConfig responseMessageConfig;
	private MessageHandlerConfig responseHandlerConfig;
	private MessageTransformerConfig errorMessageConfig;
	private MessageHandlerConfig errorHandlerConfig;
	private Map<String, List<ActionConfig>> eventConfig = new HashMap<>();
	private int timeout;
	private boolean sourceAsync = false;
	private boolean destAsync = false;
	private ErrorMappingConfig errorMappingConfig;
	private int sourceChannelMessageObjectType = 137;
	private String sourceMapCharset = System.getProperty("file.encoding");
	private String destMapCharset = System.getProperty("file.encoding");
	private int destChannelMessageObjectType = 137;
	private boolean isLocalSource = false;

	public Processor() {
	}

	public String getSourceMapCharset() {
		return this.sourceMapCharset;
	}

	public void setSourceMapCharset(String var1) {
		this.sourceMapCharset = var1;
	}

	public String getDestMapCharset() {
		return this.destMapCharset;
	}

	public void setDestMapCharset(String var1) {
		this.destMapCharset = var1;
	}

	public boolean isSourceAsync() {
		return this.sourceAsync;
	}

	public void setSourceAsync(boolean var1) {
		this.sourceAsync = var1;
	}

	public boolean isDestAsync() {
		return this.destAsync;
	}

	public void setDestAsync(boolean var1) {
		this.destAsync = var1;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String var1) {
		this.id = var1;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int var1) {
		this.type = var1;
	}

	public String getRouteRuleId() {
		return this.routeRuleId;
	}

	public void setRouteRuleId(String var1) {
		this.routeRuleId = var1;
	}

	public MessageTransformerConfig getRequestMessageConfig() {
		return this.requestMessageConfig;
	}

	public void setRequestMessageConfig(MessageTransformerConfig var1) {
		this.requestMessageConfig = var1;
	}

	public MessageHandlerConfig getRequestMessageHandlerConfig() {
		return this.requestHandlerConfig;
	}

	public void setRequestMessageHandlerConfig(MessageHandlerConfig var1) {
		this.requestHandlerConfig = var1;
	}

	public MessageTransformerConfig getResponseMessageConfig() {
		return this.responseMessageConfig;
	}

	public void setResponseMessageConfig(MessageTransformerConfig var1) {
		this.responseMessageConfig = var1;
	}

	public MessageHandlerConfig getResponseMessageHandlerConfig() {
		return this.responseHandlerConfig;
	}

	public void setResponseMessageHandlerConfig(MessageHandlerConfig var1) {
		this.responseHandlerConfig = var1;
	}

	public MessageTransformerConfig getErrorMessageConfig() {
		return this.errorMessageConfig;
	}

	public void setErrorMessageConfig(MessageTransformerConfig var1) {
		this.errorMessageConfig = var1;
	}

	public MessageHandlerConfig getErrorMessageHandlerConfig() {
		return this.errorHandlerConfig;
	}

	public void setErrorMessageHandlerConfig(MessageHandlerConfig var1) {
		this.errorHandlerConfig = var1;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int var1) {
		this.timeout = var1;
	}

	public ErrorMappingConfig getErrorMappingConfig() {
		return this.errorMappingConfig;
	}

	public void setErrorMappingConfig(ErrorMappingConfig var1) {
		this.errorMappingConfig = var1;
	}

	public int getSourceChannelMessageObjectType() {
		return this.sourceChannelMessageObjectType;
	}

	public void setSourceChannelMessageObjectType(int var1) {
		this.sourceChannelMessageObjectType = var1;
	}

	public int getDestChannelMessageObjectType() {
		return this.destChannelMessageObjectType;
	}

	public void setDestChannelMessageObjectType(int var1) {
		this.destChannelMessageObjectType = var1;
	}

	public boolean isLocalSource() {
		return this.isLocalSource;
	}

	public void setLocalSource(boolean var1) {
		this.isLocalSource = var1;
	}

	public Map<String, List<ActionConfig>> getEventConfig() {
		return this.eventConfig;
	}

	public void setEventConfig(Map<String, List<ActionConfig>> var1) {
		this.eventConfig = var1;
	}
}
