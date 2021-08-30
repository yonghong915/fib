package com.fib.msgconverter.commgateway.channel.mq.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.longconnection.config.CodeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * MQ通道配置
 * 
 * @author 白帆
 * 
 */
public class MQChannelConfig {
	public static final int TYPE_SERVER = 1001;
	public static final int TYPE_CLIENT = 1002;
	public static final int TYPE_DOUBLE = 1003;
	public static final String TYPE_SERVER_TXT = "SERVER";
	public static final String TYPE_CLIENT_TXT = "CLIENT";
	public static final String TYPE_DOUBLE_TXT = "DOUBLE";
	private Map ququeConfig = new HashMap(32);
	// private MessageKeyConfig keyConfig;
	private int ccsid = 1209;
	private int timeout = -1;
	private int type;

	private CodeRecognizerConfig codeRecognizerConfig;

	private AbstractMessageRecognizer codeRecognizer;

//	private List<AbstractMessageFilter> readerFilterList = new ArrayList<AbstractMessageFilter>();
//	private List<AbstractMessageFilter> writerFilterList = new ArrayList<AbstractMessageFilter>();
//
//	public List<AbstractMessageFilter> getReaderFilterList() {
//		return readerFilterList;
//	}
//
//	public void setReaderFilterList(List<AbstractMessageFilter> readerFilterList) {
//		this.readerFilterList = readerFilterList;
//	}
//
//	public List<AbstractMessageFilter> getWriterFilterList() {
//		return writerFilterList;
//	}
//
//	public void setWriterFilterList(List<AbstractMessageFilter> writerFilterList) {
//		this.writerFilterList = writerFilterList;
//	}
	
	private AbstractMessageFilter readerFilter;
	private AbstractMessageFilter writerFilter;

	public AbstractMessageFilter getReadFilter() {
		return readerFilter;
	}

	public void setReaderFilter(AbstractMessageFilter readFilter) {
		readerFilter = readFilter;
	}

	public AbstractMessageFilter getWriteFilter() {
		return writerFilter;
	}

	public void setWriterFilter(AbstractMessageFilter writeFilter) {
		writerFilter = writeFilter;
	}

	public AbstractMessageRecognizer getCodeRecognizer() {
		return codeRecognizer;
	}

	public void setCodeRecognizer(AbstractMessageRecognizer codeRecognizer) {
		this.codeRecognizer = codeRecognizer;
	}

	public CodeRecognizerConfig getCodeRecognizerConfig() {
		return codeRecognizerConfig;
	}

	public void setCodeRecognizerConfig(
			CodeRecognizerConfig codeRecognizerConfig) {
		this.codeRecognizerConfig = codeRecognizerConfig;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map getQuqueConfig() {
		return ququeConfig;
	}

	public void setQuqueConfig(Map ququeConfig) {
		this.ququeConfig = ququeConfig;
	}

	// public MessageKeyConfig getKeyConfig() {
	// return keyConfig;
	// }
	// public void setKeyConfig(MessageKeyConfig keyConfig) {
	// this.keyConfig = keyConfig;
	// }
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getCcsid() {
		return ccsid;
	}

	public void setCcsid(int ccsid) {
		this.ccsid = ccsid;
	}

	public static String getTypeText(int type) {
		switch (type) {
		case TYPE_SERVER:
			return TYPE_SERVER_TXT;
		case TYPE_CLIENT:
			return TYPE_CLIENT_TXT;
		case TYPE_DOUBLE:
			return TYPE_DOUBLE_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("MQChannelConfig.type.unsupport",
							new String[] { type + "" }));
		}
	}

	public static int getTypeByText(String text) {
		if (TYPE_SERVER_TXT.equalsIgnoreCase(text)) {
			return TYPE_SERVER;
		} else if (TYPE_CLIENT_TXT.equalsIgnoreCase(text)) {
			return TYPE_CLIENT;
		} else if (TYPE_DOUBLE_TXT.equalsIgnoreCase(text)) {
			return TYPE_DOUBLE;
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("MQChannelConfig.type.unsupport",
							new String[] { text }));
		}
	}
}
