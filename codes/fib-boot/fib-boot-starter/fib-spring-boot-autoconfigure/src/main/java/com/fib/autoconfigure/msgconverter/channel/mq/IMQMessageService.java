package com.fib.autoconfigure.msgconverter.channel.mq;

/**
 * MQ消息服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 15:35:00
 */
public interface IMQMessageService {
	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	void sendMessage(String destination, String message);

	/**
	 * 发送同步消息
	 * 
	 * @param id
	 * @param message
	 */
	void sendSyncMessage(String destination, String id, String message);

	/**
	 * 发送异步消息
	 * 
	 * @param id
	 * @param message
	 */
	void sendAsyncMessage(String destination, String id, String message);

	/**
	 * 发送单向消息
	 * 
	 * @param id
	 * @param message
	 */
	void sendOnewayMessage(String destination, String id, String message);

	/**
	 * 接收消息
	 * 
	 * @return
	 */
	byte[] receiveMessage();

}
