package com.fib.autoconfigure.disruptor.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.fib.autoconfigure.disruptor.event.DisruptorBindEvent;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.event.handler.chain.HandlerChain;
import com.fib.autoconfigure.disruptor.event.handler.chain.HandlerChainResolver;
import com.fib.autoconfigure.disruptor.event.handler.chain.ProxiedHandlerChain;
import com.fib.commons.disruptor.base.Session;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Disruptor 事件分发实现 消息的处理方式。消费者对于同一消息：
 * 都分别独立消费应当实现EventHandler接口；不重复消费则应当实现WorkHandler接口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-16 10:13:44
 */
public class DisruptorEventDispatcher extends AbstractRouteableEventHandler<DisruptorEvent>
		implements EventHandler<DisruptorEvent>, WorkHandler<DisruptorEvent>, Ordered {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorEventDispatcher.class);
	private int order = 0;

	public DisruptorEventDispatcher(HandlerChainResolver<DisruptorEvent> filterChainResolver, int order) {
		super(filterChainResolver);
		this.order = order;
	}

	/**
	 * 责任链入口
	 */
	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		LOGGER.info("事件分发");
		LOGGER.info("消费者处理消息开始");
		if (event != null) {
			LOGGER.info("消费者消费的信息是：{}", event);
			this.consume(event.getBody());
		}

//		// 构造原始链对象
//		HandlerChain<DisruptorEvent> originalChain = new ProxiedHandlerChain();
//		// 执行事件处理链
//		this.doHandler(event, originalChain);
//
//		if (event instanceof DisruptorBindEvent dbe) {
//			Object obj = dbe.getBind();
////			Session session = (Session) obj;
////			session.writeAndFlush("aaaa");
//		}
//
//		LOGGER.info("消费者处理消息结束");
		
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		this.onEvent(event);
	}

	public void consume(Object var1) {
		LOGGER.info("var1：{}", var1);
	}
}