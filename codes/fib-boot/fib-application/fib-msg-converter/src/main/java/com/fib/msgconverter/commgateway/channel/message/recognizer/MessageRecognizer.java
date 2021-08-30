/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午03:13:20
 */
package com.fib.msgconverter.commgateway.channel.message.recognizer;

/**
 * 消息识别器
 * @author 刘恭亮
 *
 */
public interface MessageRecognizer {
	
	/**
	 * 从报文中识别出关键数据
	 * @return
	 */
	public String recognize(byte[] message);	
	

}
