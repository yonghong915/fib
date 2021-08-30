package com.fib.msgconverter.commgateway.channel.config.processor;

public class NotifierConfig {
	private String channelSymbol;
	private String processorId;
	public String getChannelSymbol() {
		return channelSymbol;
	}
	public void setChannelSymbol(String channelSymbol) {
		this.channelSymbol = channelSymbol;
	}
	public String getProcessorId() {
		return processorId;
	}
	public void setProcessorId(String processorId) {
		this.processorId = processorId;
	}
}
