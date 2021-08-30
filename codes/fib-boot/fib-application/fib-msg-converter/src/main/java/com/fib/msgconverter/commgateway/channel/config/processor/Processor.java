package com.fib.msgconverter.commgateway.channel.config.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.processor.event.ActionConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

public class Processor {
	public static final int TYP_TRANSFORM = 153;
	public static final String TYP_TRANSFORM_TXT = "TRANSFORM";
	public static final int TYP_TRANSMIT = 152;
	public static final String TYP_TRANSMIT_TXT = "TRANSMIT";
	public static final int TYP_LOCAL = 151;
	public static final String TYP_LOCAL_TXT = "LOCAL";
	public static final int TYP_SAVE_AND_TRANSFORM = 150;
	public static final String TYP_SAVE_AND_TRANSFORM_TXT = "SAVE_AND_TRANSFORM";
	public static final int TYP_SAVE_AND_TRANSMIT = 149;
	public static final String TYP_SAVE_AND_TRANSMIT_TXT = "SAVE_AND_TRANSMIT";
	public static final int MSG_OBJ_TYP_MB = 137;
	public static final String MSG_OBJ_TYP_MB_TXT = "MESSAGE-BEAN";
	public static final int MSG_OBJ_TYP_MAP = 136;
	public static final String MSG_OBJ_TYP_MAP_TXT = "MAP";
	public static final int MSG_OBJ_TYP_JSON = 135;
	public static final String MSG_OBJ_TYP_JSON_TXT = "JSON";
	public static final int MSG_OBJ_TYP_JSON_PLUS = 134;
	public static final String MSG_OBJ_TYP_JSON_PLUS_TXT = "JSON+";
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

	public static String getTextByType(int var0) {
		switch (var0) {
		case 149:
			return "SAVE_AND_TRANSMIT";
		case 150:
			return "SAVE_AND_TRANSFORM";
		case 151:
			return "LOCAL";
		case 152:
			return "TRANSMIT";
		case 153:
			return "TRANSFORM";
		default:
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("type.unsupport", new String[] { "" + var0 }));
		}
	}

	public static int getTypeByText(String var0) {
		if ("TRANSFORM".equalsIgnoreCase(var0)) {
			return 153;
		} else if ("TRANSMIT".equalsIgnoreCase(var0)) {
			return 152;
		} else if ("LOCAL".equalsIgnoreCase(var0)) {
			return 151;
		} else if ("SAVE_AND_TRANSFORM".equals(var0)) {
			return 150;
		} else if ("SAVE_AND_TRANSMIT".equals(var0)) {
			return 149;
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("type.unsupport", new String[] { var0 }));
		}
	}

	public static int getMessageObjectTypeByText(String var0) {
		if ("MESSAGE-BEAN".equalsIgnoreCase(var0)) {
			return 137;
		} else if ("MAP".equalsIgnoreCase(var0)) {
			return 136;
		} else if ("JSON".equalsIgnoreCase(var0)) {
			return 135;
		} else if ("JSON+".equalsIgnoreCase(var0)) {
			return 134;
		} else {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("type.unsupport", new String[] { var0 }));
		}
	}

	public static String getMessageObjectText(int var0) {
		switch (var0) {
		case 134:
			return "JSON+";
		case 135:
			return "JSON";
		case 136:
			return "MAP";
		case 137:
			return "MESSAGE-BEAN";
		default:
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("type.unsupport", new String[] { var0 + "" }));
		}
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
