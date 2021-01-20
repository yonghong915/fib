package com.fib.gateway.message.xml.config.processor;

import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.config.processor.ErrorMappingConfig;
import com.fib.gateway.channel.config.processor.MessageHandlerConfig;
import com.fib.gateway.channel.config.processor.MessageTransformerConfig;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import lombok.Data;

@Data
public class Processor {

	private String id;
	private int type = 153;
	private String routeRuleId;

	private Map eventConfig = new HashMap();
	private int timeout;
	private boolean sourceAsync = false;
	private boolean destAsync = false;
	private String sourceMapCharset = System.getProperty("file.encoding");
	private String destMapCharset = System.getProperty("file.encoding");
	private int destChannelMessageObjectType = 137;
	private boolean isLocalSource = false;
	private MessageTransformerConfig requestMessageConfig;
	private MessageHandlerConfig requestMessageHandlerConfig;
	private MessageTransformerConfig responseMessageConfig;
	private MessageHandlerConfig responseMessageHandlerConfig;
	private MessageTransformerConfig errorMessageConfig;
	private MessageHandlerConfig errorMessageHandlerConfig;
	private ErrorMappingConfig errorMappingConfig;
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
	private int sourceChannelMessageObjectType = 137;

	public int getSourceChannelMessageObjectType() {
		return this.sourceChannelMessageObjectType;
	}

	public void setSourceChannelMessageObjectType(int var1) {
		this.sourceChannelMessageObjectType = var1;
	}

	public static String getTextByType(int var0) {
		switch (var0)

		{
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
}
