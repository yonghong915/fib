package com.fib.gateway.channel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fib.gateway.channel.config.recognizer.RecognizerConfig;
import com.fib.gateway.config.ChannelMainConfig;
import com.fib.gateway.config.base.TypedDynamicObjectConfig;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.config.processor.Processor;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import lombok.Data;

@Data
public class ChannelConfig extends TypedDynamicObjectConfig {
	private ChannelMainConfig mainConfig;

	/**
	 * 服务模式，SERVER=服务端 CLIENT=客户端 DOUBLE=双向
	 */
	private int mode;
	private String description;
	private ConnectorConfig connectorConfig;
	private String messageBeanGroupId;
	private String eventHandlerClazz;
	private MessageTypeRecognizerConfig messageTypeRecognizerConfig;
	private ReturnCodeRecognizerConfig returnCodeRecognizerConfig;
	private Map<String, RecognizerConfig> recognizerTable = new HashMap<>();
	private Map<String, Processor> processorTable = new HashMap<>(64);
	/**
	 * 路由表
	 */
	private Map routeTable = new HashMap(64);

	/**
	 * 通道符号表
	 */
	private Map channelSymbolTable = new HashMap();

	public static final String MODE_SERVER_TXT = "SERVER";
	public static final int MODE_SERVER = 1000;
	private static Properties prop;
	// CLIENT
	public static final String MODE_CLIENT_TXT = "CLIENT";
	public static final int MODE_CLIENT = 1001;

	// DOUBLE
	public static final String MODE_DOUBLE_TXT = "DOUBLE";
	public static final int MODE_DOUBLE = 1002;

	@Override
	protected Properties getProperties() {
		return prop;
	}

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

	public static final String DEFAULT_PROCESSOR = "DEFAULT_PROCESSOR";

	public Processor getDefaultProcessor() {
		return processorTable.get(DEFAULT_PROCESSOR);
	}

	public void setDefaultProcessor(Processor processor) {
		processorTable.put(DEFAULT_PROCESSOR, processor);
	}
}
