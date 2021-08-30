package com.fib.msgconverter.commgateway.channel.config;

/**
 * 通道符号。实际通道的别名。
 * @author 刘恭亮
 *
 */
public class ChannelSymbol {
	/**
	 * 符号
	 */
	private String symbol;
	
	/**
	 * 名
	 */
	private String name;
	
	/**
	 * 实际通道ID
	 */
	private String channlId;

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the channlId
	 */
	public String getChannlId() {
		return channlId;
	}

	/**
	 * @param channlId the channlId to set
	 */
	public void setChannlId(String channlId) {
		this.channlId = channlId;
	}

}
