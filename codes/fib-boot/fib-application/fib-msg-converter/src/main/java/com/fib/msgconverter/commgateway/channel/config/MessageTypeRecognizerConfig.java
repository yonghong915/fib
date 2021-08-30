package com.fib.msgconverter.commgateway.channel.config;

import java.util.HashMap;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;

/**
 * (请求)报文类型识别器配置：含一个报文识别器和一组报文类型与处理器的映射关系
 * 
 * @author 刘恭亮
 * 
 */
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
	public void setMessageTypeProcessorMap(Map<String, String> messageTypeProcessorMap) {
		this.messageTypeProcessorMap = messageTypeProcessorMap;
	}
}