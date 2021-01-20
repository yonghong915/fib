package com.fib.gateway.netty.disruptor;

import com.lmax.disruptor.WorkHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper> {

	@Getter
	@Setter
	protected String consumerId;

	protected MessageConsumer() {
	}

	public MessageConsumer(String consumerId) {
		this.consumerId = consumerId;
	}
}
