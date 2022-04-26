package com.fib.upp.message;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.message.bean.MessageBean;
import com.fib.upp.message.parser.AbstractMessageParser;
import com.fib.upp.message.parser.MessageParserFactory;

import cn.hutool.core.lang.Assert;

public class MessageParser extends AbstractMessageParser {
	public MessageParser() {
		//
	}

	@Override
	public MessageBean parse() {
		Assert.notNull(message, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "message"));
		Assert.notNull(messageData, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, "messageData"));

		AbstractMessageParser delegate = MessageParserFactory.getMessageParser(message);
		delegate.setMessage(message);
		delegate.setMessageData(messageData);
		delegate.setMessageBean(messageBean);
		delegate.setDefaultCharset(getDefaultCharset());
		return delegate.parse();
	}
}