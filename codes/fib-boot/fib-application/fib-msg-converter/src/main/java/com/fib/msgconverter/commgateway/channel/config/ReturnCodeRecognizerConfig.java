package com.fib.msgconverter.commgateway.channel.config;

import java.util.HashSet;
import java.util.Set;

import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;

/**
 * (应答报文)返回码识别器配置：含一个报文识别器和两个集合，成功返回码集合和失败返回码集合
 * 
 * @author 刘恭亮
 *
 */
public class ReturnCodeRecognizerConfig {
	/**
	 * 识别器ID
	 */
	private String recognizerId;

	/**
	 * 识别器配置
	 */
	private RecognizerConfig recognizerConfig;

	/**
	 * 成功返回码集合
	 */
	private Set<String> successCodeSet = new HashSet<>();

	/**
	 * 失败返回码集合
	 */
	private Set<String> errorCodeSet;

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
	 * @return the successCodeSet
	 */
	public Set<String> getSuccessCodeSet() {
		return successCodeSet;
	}

	/**
	 * @param successCodeSet the successCodeSet to set
	 */
	public void setSuccessCodeSet(Set<String> successCodeSet) {
		this.successCodeSet = successCodeSet;
	}

	/**
	 * @return the errorCodeSet
	 */
	public Set<String> getErrorCodeSet() {
		return errorCodeSet;
	}

	/**
	 * @param errorCodeSet the errorCodeSet to set
	 */
	public void setErrorCodeSet(Set<String> errorCodeSet) {
		this.errorCodeSet = errorCodeSet;
	}
}