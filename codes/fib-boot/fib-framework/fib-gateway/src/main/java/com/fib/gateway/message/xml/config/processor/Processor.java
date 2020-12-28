package com.fib.gateway.message.xml.config.processor;

public class Processor {
	private Processor() {
		//
	}

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
}
