package com.fib.autoconfigure.msgconverter.channel.config;

import java.util.HashSet;
import java.util.Set;

import com.fib.autoconfigure.msgconverter.channel.config.recognizer.RecognizerConfig;

/**
 * 返回码识别器配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 16:40:21
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