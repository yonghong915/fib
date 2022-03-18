package com.fib.commons.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 11:16:50
 */
public final class MsgEventFactory implements EventFactory<MsgEvent> {
	public static final MsgEventFactory DEFAULT = new MsgEventFactory();

	@Override
	public MsgEvent newInstance() {
		return new MsgEvent();
	}
}
