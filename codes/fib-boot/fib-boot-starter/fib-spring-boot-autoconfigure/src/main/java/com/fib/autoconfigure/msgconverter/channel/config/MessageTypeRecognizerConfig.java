package com.fib.autoconfigure.msgconverter.channel.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fib.autoconfigure.msgconverter.channel.config.recognizer.RecognizerConfig;

/**
 * 报文类型识别器配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 16:39:52
 */
public class MessageTypeRecognizerConfig {
	/** 识别器ID */
	private String recognizerId;

	/** 识别器配置 */
	private RecognizerConfig recognizerConfig;

	/** 消息类型与处理器映射,key=messageType,value=processorId */
	private Map<String, String> messageTypeProcessorMap = new HashMap<>(32);

	public String getRecognizerId() {
		return recognizerId;
	}

	public void setRecognizerId(String recognizerId) {
		this.recognizerId = recognizerId;
	}

	public RecognizerConfig getRecognizerConfig() {
		return recognizerConfig;
	}

	public void setRecognizerConfig(RecognizerConfig recognizerConfig) {
		this.recognizerConfig = recognizerConfig;
	}

	public Map<String, String> getMessageTypeProcessorMap() {
		return messageTypeProcessorMap;
	}

	public void setMessageTypeProcessorMap(Map<String, String> messageTypeProcessorMap) {
		this.messageTypeProcessorMap = messageTypeProcessorMap;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}