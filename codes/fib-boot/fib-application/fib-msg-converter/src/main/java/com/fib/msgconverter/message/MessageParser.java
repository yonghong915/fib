package com.fib.msgconverter.message;

import com.fib.msgconverter.message.bean.MessageBean;
import com.fib.msgconverter.message.parser.AbstractMessageParser;
import com.fib.msgconverter.message.parser.MessageParserFactory;
import com.giantstone.common.multilang.MultiLanguageResourceBundle;

/**
 * 报文解析器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessageParser extends AbstractMessageParser {
	public MessageBean parse() {
		AbstractMessageParser delegate;
		if (null == message)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "message" }));
		if (null == messageData) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "messageData" }));
		} else {
			delegate = MessageParserFactory.getMessageParser(message);
			delegate.setMessage(message);
			delegate.setMessageData(messageData);
			delegate.setMessageBean(messageBean);
			delegate.setDefaultCharset(getDefaultCharset());
			return delegate.parse();
		}
	}

	@Override
	protected int parse(int idx) {
		return 0;
	}
}