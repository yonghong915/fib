package com.fib.gateway.channel.message.recognizer;

public interface MessageRecognizer {

	/**
	 * 从报文中识别出关键数据
	 * 
	 * @return
	 */
	public String recognize(byte[] message);

}
