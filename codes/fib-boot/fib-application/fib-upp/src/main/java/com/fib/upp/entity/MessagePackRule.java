package com.fib.upp.entity;

/**
 * 小额报文组包规则
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-22 11:10:51
 */
public class MessagePackRule {

	/** 报文类型 */
	private String messageTypeCode;

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}
}