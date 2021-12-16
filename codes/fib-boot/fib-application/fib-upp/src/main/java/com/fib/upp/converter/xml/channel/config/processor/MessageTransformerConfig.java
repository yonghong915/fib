/**
 * 北京长信通信息技术有限公司
 * 2008-11-24 下午08:05:37
 */
package com.fib.upp.converter.xml.channel.config.processor;

/**
 * 报文配置：源通道、目的通道MessageBean ID，及两者的映射配置ID
 * 
 * @author 刘恭亮
 * 
 */
public class MessageTransformerConfig {

	/**
	 * 源通道请求/应答报文(message-bean)Id
	 */
	private String sourceMessageId;

	/**
	 * 目的通道请求/应答报文(message-bean)Id
	 */
	private String destinationMessageId;

	/**
	 * 源/目的通道 请求/应答报文MB 与 目的/源通道 请求/应答报文MB 的映射配置Id
	 */
	private String mappingId;

	/**
	 * @return the sourceMessageId
	 */
	public String getSourceMessageId() {
		return sourceMessageId;
	}

	/**
	 * @param sourceMessageId the sourceMessageId to set
	 */
	public void setSourceMessageId(String sourceMessageId) {
		this.sourceMessageId = sourceMessageId;
	}

	/**
	 * @return the destinationMessageId
	 */
	public String getDestinationMessageId() {
		return destinationMessageId;
	}

	/**
	 * @param destinationMessageId the destinationMessageId to set
	 */
	public void setDestinationMessageId(String destinationMessageId) {
		this.destinationMessageId = destinationMessageId;
	}

	/**
	 * @return the mappingId
	 */
	public String getMappingId() {
		return mappingId;
	}

	/**
	 * @param mappingId the mappingId to set
	 */
	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

}
