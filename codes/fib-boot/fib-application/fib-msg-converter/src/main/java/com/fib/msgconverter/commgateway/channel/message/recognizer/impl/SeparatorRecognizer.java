/**
 * 北京长信通信息技术有限公司
 * 2008-11-24 上午10:09:18
 */
package com.fib.msgconverter.commgateway.channel.message.recognizer.impl;

import java.util.Map;
import java.util.Properties;

import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.message.util.FieldDataLocator;

/**
 * 分隔符定位方式的识别器。根据域的前后缀、域数据类型定义等配置从报文中取得域值。
 * @author 刘恭亮
 *
 */
public class SeparatorRecognizer extends AbstractMessageRecognizer{
	private FieldDataLocator locator;
	
	
	public SeparatorRecognizer(){
		locator = new FieldDataLocator();
	}
	
	public String recognize(byte[] message) {
		return locator.locateAsString(message);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		Properties prop = new Properties();
		prop.putAll(parameters);
		prop.setProperty(FieldDataLocator.LOCATE_METHOD_TEXT,
				FieldDataLocator.LOCATE_METHOD_SEPARATOR_TEXT);

		locator.setProperties(prop);

	}
	
	public Map getParameter(){
		return parameters;
	}

}
