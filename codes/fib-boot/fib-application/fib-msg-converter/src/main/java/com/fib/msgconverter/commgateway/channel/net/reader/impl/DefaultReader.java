package com.fib.msgconverter.commgateway.channel.net.reader.impl;


import com.fib.msgconverter.commgateway.channel.net.reader.AbstractReader;

/**
 * 默认的消息读取器。只读一次，读多少算多少
 * 
 * @author 刘恭亮
 * 
 */
public class DefaultReader extends AbstractReader {

	public boolean checkMessageComplete(byte[] message) {
		return true;
	}
	
}
