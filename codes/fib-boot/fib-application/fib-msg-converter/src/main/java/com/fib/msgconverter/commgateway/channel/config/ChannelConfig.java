package com.fib.msgconverter.commgateway.channel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.config.route.RouteRule;
import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.config.base.TypedDynamicObjectConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 通道配置
 * 
 * @author 刘恭亮
 * 
 */
public class ChannelConfig extends TypedDynamicObjectConfig {
	/**
	 * 主信息：id、name、deploy等
	 */
	private ChannelMainConfig mainConfig;

	/**
	 * 服务模式，SERVER=服务端 CLIENT=客户端 DOUBLE=双向
	 */
	private int mode;

	// SERVER
	public static final String MODE_SERVER_TXT = "SERVER";
	public static final int MODE_SERVER = 1000;

	// CLIENT
	public static final String MODE_CLIENT_TXT = "CLIENT";
	public static final int MODE_CLIENT = 1001;

	// DOUBLE
	public static final String MODE_DOUBLE_TXT = "DOUBLE";
	public static final int MODE_DOUBLE = 1002;

	/**
	 * 详细描述信息
	 */
	private String description;

	/**
	 * 事件处理器实现类
	 */
	private String eventHandlerClazz;

	/**
	 * 连接器配置
	 */
	private ConnectorConfig connectorConfig;

	/**
	 * 报文类型识别器配置
	 */
	private MessageTypeRecognizerConfig messageTypeRecognizerConfig;

	/**
	 * 返回码识别器配置
	 */
	private ReturnCodeRecognizerConfig returnCodeRecognizerConfig;

	/**
	 * 报文识别器表
	 */
	private Map<String, RecognizerConfig> recognizerTable = new HashMap<>();

	/**
	 * 报文处理器表
	 */
	private Map<String, Processor> processorTable = new HashMap<>(64);

	// 默认处理器ID
	public static final String DEFAULT_PROCESSOR = "DEFAULT_PROCESSOR                                                                ";

	/**
	 * 路由表
	 */
	private Map<String, RouteRule> routeTable = new HashMap<>(64);

	/**
	 * 通道符号表
	 */
	private Map<String, ChannelSymbol> channelSymbolTable = new HashMap<>();

	/**
	 * 通道加载的MB组
	 */
	private String messageBeanGroupId;

	/**
	 * 连接器ID，数据库专用
	 */
	private String connectorId;

	/**
	 * @return the mainConfig
	 */
	public ChannelMainConfig getMainConfig() {
		return mainConfig;
	}

	/**
	 * @param mainConfig the mainConfig to set
	 */
	public void setMainConfig(ChannelMainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the connectorConfig
	 */
	public ConnectorConfig getConnectorConfig() {
		return connectorConfig;
	}

	/**
	 * @param connectorConfig the connectorConfig to set
	 */
	public void setConnectorConfig(ConnectorConfig connectorConfig) {
		this.connectorConfig = connectorConfig;
	}

	/**
	 * @return the messageTypeRecognizerConfig
	 */
	public MessageTypeRecognizerConfig getMessageTypeRecognizerConfig() {
		return messageTypeRecognizerConfig;
	}

	/**
	 * @param messageTypeRecognizerConfig the messageTypeRecognizerConfig to set
	 */
	public void setMessageTypeRecognizerConfig(MessageTypeRecognizerConfig messageTypeRecognizerConfig) {
		this.messageTypeRecognizerConfig = messageTypeRecognizerConfig;
	}

	/**
	 * @return the returnCodeRecognizerConfig
	 */
	public ReturnCodeRecognizerConfig getReturnCodeRecognizerConfig() {
		return returnCodeRecognizerConfig;
	}

	/**
	 * @param returnCodeRecognizerConfig the returnCodeRecognizerConfig to set
	 */
	public void setReturnCodeRecognizerConfig(ReturnCodeRecognizerConfig returnCodeRecognizerConfig) {
		this.returnCodeRecognizerConfig = returnCodeRecognizerConfig;
	}

	/**
	 * @return the recognizerTable
	 */
	public Map<String, RecognizerConfig> getRecognizerTable() {
		return recognizerTable;
	}

	/**
	 * @param recognizerTable the recognizerTable to set
	 */
	public void setRecognizerTable(Map<String, RecognizerConfig> recognizerTable) {
		this.recognizerTable = recognizerTable;
	}

	/**
	 * @return the processorTable
	 */
	public Map<String, Processor> getProcessorTable() {
		return processorTable;
	}

	/**
	 * @param processorTable the processorTable to set
	 */
	public void setProcessorTable(Map<String, Processor> processorTable) {
		this.processorTable = processorTable;
	}

	public void setDefaultProcessor(Processor processor) {
		processorTable.put(DEFAULT_PROCESSOR, processor);
	}

	public Processor getDefaultProcessor() {
		return (Processor) processorTable.get(DEFAULT_PROCESSOR);
	}

	/**
	 * @return the routeTable
	 */
	public Map<String, RouteRule> getRouteTable() {
		return routeTable;
	}

	/**
	 * @param routeTable the routeTable to set
	 */
	public void setRouteTable(Map<String, RouteRule> routeTable) {
		this.routeTable = routeTable;
	}

	/**
	 * @return the channelSymbolTable
	 */
	public Map<String, ChannelSymbol> getChannelSymbolTable() {
		return channelSymbolTable;
	}

	/**
	 * @param channelSymbolTable the channelSymbolTable to set
	 */
	public void setChannelSymbolTable(Map<String, ChannelSymbol> channelSymbolTable) {
		this.channelSymbolTable = channelSymbolTable;
	}

	private static Properties prop;

	static {
		String propFileName = "channelType.properties";
		InputStream in = ChannelConfig.class.getResourceAsStream(propFileName);
		if (null == in) {
			// throw new RuntimeException("Can't load " + propFileName);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("propery.canNotLoadFile",
					new String[] { propFileName }));
		}
		prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	protected Properties getProperties() {
		return prop;
	}

	public String getEventHandlerClazz() {
		return eventHandlerClazz;
	}

	public void setEventHandlerClazz(String eventHandlerClazz) {
		this.eventHandlerClazz = eventHandlerClazz;
	}

	public String getMessageBeanGroupId() {
		return messageBeanGroupId;
	}

	public void setMessageBeanGroupId(String messageBeanGroupId) {
		this.messageBeanGroupId = messageBeanGroupId;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public static int getModeByText(String text) {
		if (MODE_SERVER_TXT.equals(text)) {
			return MODE_SERVER;
		} else if (MODE_CLIENT_TXT.equals(text)) {
			return MODE_CLIENT;
		} else if (MODE_DOUBLE_TXT.equals(text)) {
			return MODE_DOUBLE;
		} else {
			// throw new RuntimeException("Unsupport Mode :" + text);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("ChannelConfig.getModeByText.mode.unsupport", new String[] { text }));
		}
	}

	public static String getModeText(int mode) {
		switch (mode) {
		case MODE_SERVER:
			return MODE_SERVER_TXT;
		case MODE_CLIENT:
			return MODE_CLIENT_TXT;
		case MODE_DOUBLE:
			return MODE_DOUBLE_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("ChannelConfig.getModeByText.mode.unsupport", new String[] { mode + "" }));
		}
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

}
