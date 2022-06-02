package com.fib.autoconfigure.msgconverter.channel.config.route;

import com.fib.autoconfigure.msgconverter.channel.config.processor.Processor;

/**
 * 决定。动态路由表达式的执行结果对应的处理配置。
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-26 11:07:39
 */
public class Determination {
	/**
	 * 表达式的一种结果
	 */
	private String result;

	/**
	 * 目的通道符号
	 */
	private String destinationChannelSymbol;

	/**
	 * 处理器覆盖。除request-message/@source-message-id外，其属性覆盖标准的处理器属性。
	 */
	private Processor processorOverride;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the destinationChannelSymbol
	 */
	public String getDestinationChannelSymbol() {
		return destinationChannelSymbol;
	}

	/**
	 * @param destinationChannelSymbol the destinationChannelSymbol to set
	 */
	public void setDestinationChannelSymbol(String destinationChannelSymbol) {
		this.destinationChannelSymbol = destinationChannelSymbol;
	}

	/**
	 * @return the processorOverride
	 */
	public Processor getProcessorOverride() {
		return processorOverride;
	}

	/**
	 * @param processorOverride the processorOverride to set
	 */
	public void setProcessorOverride(Processor processorOverride) {
		this.processorOverride = processorOverride;
	}
}
