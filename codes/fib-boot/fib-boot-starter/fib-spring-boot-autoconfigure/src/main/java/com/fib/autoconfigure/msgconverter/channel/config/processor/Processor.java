package com.fib.autoconfigure.msgconverter.channel.config.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fib.autoconfigure.msgconverter.util.EnumConstants;

/**
 * 处理器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 14:41:53
 */
public class Processor {
	private String id;
	private EnumConstants.ProcessorType type = EnumConstants.ProcessorType.TRANSFORM;
	private String routeRuleId;
	private MessageTransformerConfig requestMessageConfig;
	private MessageHandlerConfig requestHandlerConfig;
	private MessageTransformerConfig responseMessageConfig;
	private MessageHandlerConfig responseHandlerConfig;
	private MessageTransformerConfig errorMessageConfig;
	private MessageHandlerConfig errorHandlerConfig;
	private Map<String, Object> eventConfig = new HashMap<>();
	private int timeout;
	private boolean sourceAsync = false;
	private boolean destAsync = false;
	private ErrorMappingConfig errorMappingConfig;
	private EnumConstants.MessageObjectType sourceChannelMessageObjectType = EnumConstants.MessageObjectType.MESSAGE_BEAN;
	private String sourceMapCharset = System.getProperty("file.encoding");
	private String destMapCharset = System.getProperty("file.encoding");
	private EnumConstants.MessageObjectType destChannelMessageObjectType = EnumConstants.MessageObjectType.MESSAGE_BEAN;
	private boolean isLocalSource = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EnumConstants.ProcessorType getType() {
		return type;
	}

	public void setType(EnumConstants.ProcessorType type) {
		this.type = type;
	}

	public String getRouteRuleId() {
		return routeRuleId;
	}

	public void setRouteRuleId(String routeRuleId) {
		this.routeRuleId = routeRuleId;
	}

	public MessageTransformerConfig getRequestMessageConfig() {
		return requestMessageConfig;
	}

	public void setRequestMessageConfig(MessageTransformerConfig requestMessageConfig) {
		this.requestMessageConfig = requestMessageConfig;
	}

	public MessageHandlerConfig getRequestMessageHandlerConfig() {
		return requestHandlerConfig;
	}

	public void setRequestMessageHandlerConfig(MessageHandlerConfig requestHandlerConfig) {
		this.requestHandlerConfig = requestHandlerConfig;
	}

	public MessageTransformerConfig getResponseMessageConfig() {
		return responseMessageConfig;
	}

	public void setResponseMessageConfig(MessageTransformerConfig responseMessageConfig) {
		this.responseMessageConfig = responseMessageConfig;
	}

	public MessageHandlerConfig getResponseHandlerConfig() {
		return responseHandlerConfig;
	}

	public void setResponseHandlerConfig(MessageHandlerConfig responseHandlerConfig) {
		this.responseHandlerConfig = responseHandlerConfig;
	}

	public MessageTransformerConfig getErrorMessageConfig() {
		return errorMessageConfig;
	}

	public void setErrorMessageConfig(MessageTransformerConfig errorMessageConfig) {
		this.errorMessageConfig = errorMessageConfig;
	}

	public MessageHandlerConfig getErrorHandlerConfig() {
		return errorHandlerConfig;
	}

	public void setErrorHandlerConfig(MessageHandlerConfig errorHandlerConfig) {
		this.errorHandlerConfig = errorHandlerConfig;
	}

	public Map<String, Object> getEventConfig() {
		return eventConfig;
	}

	public void setEventConfig(Map<String, Object> eventConfig) {
		this.eventConfig = eventConfig;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isSourceAsync() {
		return sourceAsync;
	}

	public void setSourceAsync(boolean sourceAsync) {
		this.sourceAsync = sourceAsync;
	}

	public boolean isDestAsync() {
		return destAsync;
	}

	public void setDestAsync(boolean destAsync) {
		this.destAsync = destAsync;
	}

	public ErrorMappingConfig getErrorMappingConfig() {
		return errorMappingConfig;
	}

	public void setErrorMappingConfig(ErrorMappingConfig errorMappingConfig) {
		this.errorMappingConfig = errorMappingConfig;
	}

	public EnumConstants.MessageObjectType getSourceChannelMessageObjectType() {
		return sourceChannelMessageObjectType;
	}

	public void setSourceChannelMessageObjectType(EnumConstants.MessageObjectType sourceChannelMessageObjectType) {
		this.sourceChannelMessageObjectType = sourceChannelMessageObjectType;
	}

	public String getSourceMapCharset() {
		return sourceMapCharset;
	}

	public void setSourceMapCharset(String sourceMapCharset) {
		this.sourceMapCharset = sourceMapCharset;
	}

	public String getDestMapCharset() {
		return destMapCharset;
	}

	public void setDestMapCharset(String destMapCharset) {
		this.destMapCharset = destMapCharset;
	}

	public EnumConstants.MessageObjectType getDestChannelMessageObjectType() {
		return destChannelMessageObjectType;
	}

	public void setDestChannelMessageObjectType(EnumConstants.MessageObjectType destChannelMessageObjectType) {
		this.destChannelMessageObjectType = destChannelMessageObjectType;
	}

	public boolean isLocalSource() {
		return isLocalSource;
	}

	public void setLocalSource(boolean isLocalSource) {
		this.isLocalSource = isLocalSource;
	}

	public MessageHandlerConfig getResponseMessageHandlerConfig() {
		return this.responseHandlerConfig;
	}

	public void setResponseMessageHandlerConfig(MessageHandlerConfig var1) {
		this.responseHandlerConfig = var1;
	}

	public MessageHandlerConfig getErrorMessageHandlerConfig() {
		return this.errorHandlerConfig;
	}

	public void setErrorMessageHandlerConfig(MessageHandlerConfig var1) {
		this.errorHandlerConfig = var1;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}