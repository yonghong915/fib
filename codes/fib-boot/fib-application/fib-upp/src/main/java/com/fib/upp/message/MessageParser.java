package com.fib.upp.message;

import com.fib.commons.exception.CommonException;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.parser.AbstractMessageParser;
import com.fib.upp.message.parser.MessageParserFactory;

import cn.hutool.core.lang.Assert;

/**
 * 报文解析器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-29
 */
public class MessageParser extends AbstractMessageParser {
	public MessageBean parse() {
		Assert.notNull(message, () -> new CommonException("parameter.null.message"));
		Assert.notNull(messageData, () -> new CommonException("parameter.null.messageData"));

		AbstractMessageParser delegate = MessageParserFactory.getMessageParser(message);
		delegate.setMessage(message);
		delegate.setMessageData(messageData);
		delegate.setMessageBean(messageBean);
		delegate.setDefaultCharset(getDefaultCharset());
		return delegate.parse();
	}
}