package com.fib.autoconfigure.msgconverter.channel.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fib.autoconfigure.msgconverter.channel.Channel;
import com.fib.autoconfigure.msgconverter.channel.config.processor.Processor;
import com.fib.autoconfigure.msgconverter.channel.config.recognizer.RecognizerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.route.RouteRule;
import com.fib.autoconfigure.msgconverter.gateway.config.base.TypedDynamicObjectConfig;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;

/**
 * 通道配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 16:21:14
 */
public class ChannelConfig extends TypedDynamicObjectConfig {

	/** 主信息 */
	private ChannelMainConfig mainConfig;

	/** 服务模式，SERVER=服务端 CLIENT=客户端 DOUBLE=双向 */
	private ConstantMB.ChannelMode mode;

	/** 描述信息 */
	private String description;

	/** 事件处理器实现类 */
	private String eventHandlerClass;

	/** 通道加载的MB组 */
	private String messageBeanGroupId;

	/** 连接器配置 */
	private ConnectorConfig connectorConfig;

	/** 报文类型识别器配置 */
	private MessageTypeRecognizerConfig messageTypeRecognizerConfig;

	/** 返回码识别器配置 */
	private ReturnCodeRecognizerConfig returnCodeRecognizerConfig;

	/** 报文识别器表 */
	private Map<String, RecognizerConfig> recognizerTable = new HashMap<>(64);

	/** 报文处理器表 */
	private Map<String, Processor> processorTable = new HashMap<>(64);

	// 默认处理器ID
	public static final String DEFAULT_PROCESSOR = "DEFAULT_PROCESSOR                                                                ";

	/** 路由表 */
	private Map<String, RouteRule> routeTable = new HashMap<>(64);

	/** 通道符号表 */
	private Map<String, ChannelSymbol> channelSymbolTable = new HashMap<>();

	private String charset;

	public ChannelMainConfig getMainConfig() {
		return mainConfig;
	}

	public void setMainConfig(ChannelMainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	public ConstantMB.ChannelMode getMode() {
		return mode;
	}

	public void setMode(ConstantMB.ChannelMode mode) {
		this.mode = mode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventHandlerClass() {
		return eventHandlerClass;
	}

	public void setEventHandlerClass(String eventHandlerClass) {
		this.eventHandlerClass = eventHandlerClass;
	}

	public String getMessageBeanGroupId() {
		return messageBeanGroupId;
	}

	public void setMessageBeanGroupId(String messageBeanGroupId) {
		this.messageBeanGroupId = messageBeanGroupId;
	}

	public ConnectorConfig getConnectorConfig() {
		return connectorConfig;
	}

	public void setConnectorConfig(ConnectorConfig connectorConfig) {
		this.connectorConfig = connectorConfig;
	}

	public MessageTypeRecognizerConfig getMessageTypeRecognizerConfig() {
		return messageTypeRecognizerConfig;
	}

	public void setMessageTypeRecognizerConfig(MessageTypeRecognizerConfig messageTypeRecognizerConfig) {
		this.messageTypeRecognizerConfig = messageTypeRecognizerConfig;
	}

	public ReturnCodeRecognizerConfig getReturnCodeRecognizerConfig() {
		return returnCodeRecognizerConfig;
	}

	public void setReturnCodeRecognizerConfig(ReturnCodeRecognizerConfig returnCodeRecognizerConfig) {
		this.returnCodeRecognizerConfig = returnCodeRecognizerConfig;
	}

	public Map<String, RecognizerConfig> getRecognizerTable() {
		return recognizerTable;
	}

	public void setRecognizerTable(Map<String, RecognizerConfig> recognizerTable) {
		this.recognizerTable = recognizerTable;
	}

	public Map<String, Processor> getProcessorTable() {
		return processorTable;
	}

	public void setProcessorTable(Map<String, Processor> processorTable) {
		this.processorTable = processorTable;
	}

	public void setDefaultProcessor(Processor processor) {
		processorTable.put(DEFAULT_PROCESSOR, processor);
	}

	public Processor getDefaultProcessor() {
		return processorTable.get(DEFAULT_PROCESSOR);
	}

	public Map<String, RouteRule> getRouteTable() {
		return routeTable;
	}

	public void setRouteTable(Map<String, RouteRule> routeTable) {
		this.routeTable = routeTable;
	}

	public Map<String, ChannelSymbol> getChannelSymbolTable() {
		return channelSymbolTable;
	}

	public void setChannelSymbolTable(Map<String, ChannelSymbol> channelSymbolTable) {
		this.channelSymbolTable = channelSymbolTable;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	protected void setObjInterface() {
		this.objInterface = Channel.class;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
