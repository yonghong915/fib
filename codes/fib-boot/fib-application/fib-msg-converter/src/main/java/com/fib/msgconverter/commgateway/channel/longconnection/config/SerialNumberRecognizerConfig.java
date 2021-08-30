package com.fib.msgconverter.commgateway.channel.longconnection.config;

import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;

public class SerialNumberRecognizerConfig {
	private String recognizerId;
	private RecognizerConfig recognizerConfig;
	
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
	
}
