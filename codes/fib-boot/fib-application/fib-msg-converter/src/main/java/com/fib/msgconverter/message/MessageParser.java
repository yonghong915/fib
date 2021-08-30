package com.fib.msgconverter.message;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.message.bean.MessageBean;
import com.fib.msgconverter.message.parser.AbstractMessageParser;
import com.fib.msgconverter.message.parser.MessageParserFactory;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessageParser extends AbstractMessageParser {

	private AbstractMessageParser delegate;

	public MessageParser() {
	}

	public MessageBean parse() {
		if (null == message)
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		if (null == messageData) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageData" }));
		} else {
			delegate = MessageParserFactory.A(message);
			delegate.setMessage(message);
			delegate.setMessageData(messageData);
			delegate.setMessageBean(messageBean);
			delegate.setDefaultCharset(getDefaultCharset());
			return delegate.parse();
		}
	}
}