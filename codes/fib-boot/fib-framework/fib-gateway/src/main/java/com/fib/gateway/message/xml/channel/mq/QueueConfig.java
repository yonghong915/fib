package com.fib.gateway.message.xml.channel.mq;

import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class QueueConfig {
	public static final int QUEUE_TYPE_SEND = 2001;
	public static final int QUEUE_TYPE_RECEIVE = 2002;
	public static final String QUEUE_TYPE_SEND_TXT= "send";
	public static final String QUEUE_TYPE_RECEIVE_TXT = "receive";
	private int type;
	private String serverAddress;
	private String serverAddressString;
	private int port;
	private String portString;
	private String queueManager;
	private String name;
	private String channel;
	private String messageKeyRecognizerId;
	private AbstractMessageRecognizer messageKeyRecognizer;
	
	private byte[] relationMessageId;

	public byte[] getRelationMessageId() {
		return relationMessageId;
	}

	public void setRelationMessageId(byte[] relationMessageId) {
		this.relationMessageId = relationMessageId;
	}

	
	public String getServerAddressString() {
		return serverAddressString;
	}

	public void setServerAddressString(String serverAddressString) {
		this.serverAddressString = serverAddressString;
	}

	public AbstractMessageRecognizer getMessageKeyRecognizer() {
		return messageKeyRecognizer;
	}

	public void setMessageKeyRecognizer(
			AbstractMessageRecognizer messageKeyRecognizer) {
		this.messageKeyRecognizer = messageKeyRecognizer;
	}

	public String getMessageKeyRecognizerId() {
		return messageKeyRecognizerId;
	}

	public void setMessageKeyRecognizerId(String messageKeyRecognizerId) {
		this.messageKeyRecognizerId = messageKeyRecognizerId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPortString() {
		return portString;
	}

	public void setPortString(String portString) {
		this.portString = portString;
	}

	public static String getQueueText(int queueType) {
		switch (queueType) {
		case QUEUE_TYPE_SEND:
			return QUEUE_TYPE_SEND_TXT;
		case QUEUE_TYPE_RECEIVE:
			return QUEUE_TYPE_RECEIVE_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannelConfig.queueType.unsupport",
							new String[] { queueType + "" }));
		}
	}

	public static int getQueueTypeByText(String text) {
		if (QUEUE_TYPE_SEND_TXT.equalsIgnoreCase(text)) {
			return QUEUE_TYPE_SEND;
		} else if (QUEUE_TYPE_RECEIVE_TXT.equalsIgnoreCase(text)) {
			return QUEUE_TYPE_RECEIVE;
		} else {
			// throw new RuntimeException("Unsupported queue type: " + text
			// + "!It must be 'send' or 'receive'!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannelConfig.queueType.unsupport",
							new String[] { text }));
		}
	}

}
