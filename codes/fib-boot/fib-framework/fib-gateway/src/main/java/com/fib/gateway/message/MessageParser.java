package com.fib.gateway.message;

import java.util.Objects;

import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.parser.AbstractMessageParser;
import com.fib.gateway.message.parser.MessageParserFactory;

/**
 * 报文解析器
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-30
 */
public class MessageParser extends AbstractMessageParser {

	@Override
	public MessageBean parse() {
		AbstractMessageParser delegate;
		if (Objects.isNull(this.message)) {
			throw new IllegalArgumentException("");
		}
		if (Objects.isNull(this.messageData)) {
			throw new IllegalArgumentException("");
		}
		delegate = MessageParserFactory.getMessageParser(this.message);
		delegate.setMessage(this.message);
		delegate.setFieldIndexCache(this.fieldIndexCache);
		delegate.setNeedIndex(this.needIndex);
		delegate.setMessageData(this.messageData);
		delegate.setMessageBean(this.messageBean);
		delegate.setNeedIndex(this.needIndex);
		delegate.setIndexOffSet(this.indexOffSet);
		delegate.setFieldIndexCache(this.fieldIndexCache);
		return delegate.parse();
	}

}
