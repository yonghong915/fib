package com.fib.gateway.channel.config;

import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.channel.config.recognizer.RecognizerConfig;

public class MessageTypeRecognizerConfig {
	/**
	 * 识别器ID
	 */
	private String recognizerId;

	/**
	 * 识别器配置
	 */
	private RecognizerConfig recognizerConfig;

	/**
	 * 消息类型与处理器映射
	 */
	private Map<String, String> messageTypeProcessorMap = new HashMap<>(32);

	/**
	 * @return the recognizerId
	 */
	public String getRecognizerId() {
		return recognizerId;
	}

	/**
	 * @param recognizerId the recognizerId to set
	 */
	public void setRecognizerId(String recognizerId) {
		this.recognizerId = recognizerId;
	}

	/**
	 * @return the recognizerConfig
	 */
	public RecognizerConfig getRecognizerConfig() {
		return recognizerConfig;
	}

	/**
	 * @param recognizerConfig the recognizerConfig to set
	 */
	public void setRecognizerConfig(RecognizerConfig recognizerConfig) {
		this.recognizerConfig = recognizerConfig;
	}

	/**
	 * @return the messageTypeProcessorMap
	 */
	public Map<String, String> getMessageTypeProcessorMap() {
		return messageTypeProcessorMap;
	}

	/**
	 * @param messageTypeProcessorMap the messageTypeProcessorMap to set
	 */
	public void setMessageTypeProcessorMap(Map messageTypeProcessorMap) {
		this.messageTypeProcessorMap = messageTypeProcessorMap;
	}

}
