package com.fib.autoconfigure.disruptor;

import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.event.factory.DisruptorBindEventFactory;
import com.fib.autoconfigure.disruptor.event.factory.DisruptorEventThreadFactory;
import com.fib.autoconfigure.disruptor.event.handler.DisruptorEventHandler;
import com.fib.autoconfigure.disruptor.event.handler.EventExceptionHandler;
import com.fib.autoconfigure.disruptor.event.translator.DisruptorEventOneArgTranslator;
import com.fib.autoconfigure.disruptor.util.WaitStrategys;
import com.fib.autoconfigure.util.PrefixUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor自动配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-18 10:33:08
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fib.autoconfigure.disruptor")
@ConditionalOnClass({ Disruptor.class })
@ConditionalOnProperty(prefix = PrefixUtil.DISRUPTOR_PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ DisruptorProperties.class })
public class DisruptorAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public WaitStrategy waitStrategy() {
		return WaitStrategys.YIELDING_WAIT;
	}

	@Bean
	@ConditionalOnMissingBean
	public ThreadFactory threadFactory() {
		return new DisruptorEventThreadFactory();
	}

	@Bean
	@ConditionalOnMissingBean
	public EventFactory<DisruptorEvent> eventFactory() {
		return new DisruptorBindEventFactory();
	}

	@Bean
	@ConditionalOnMissingBean
	public EventTranslatorOneArg<DisruptorEvent, DisruptorEvent> oneArgEventTranslator() {
		return new DisruptorEventOneArgTranslator();
	}

	@Bean
	@ConditionalOnClass({ Disruptor.class })
	@ConditionalOnProperty(prefix = PrefixUtil.DISRUPTOR_PREFIX, value = "enabled", havingValue = "true")
	public Disruptor<DisruptorEvent> disruptor(DisruptorProperties properties, WaitStrategy waitStrategy, ThreadFactory threadFactory,
			EventFactory<DisruptorEvent> eventFactory) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("build and start disruptor.");
		}
		Disruptor<DisruptorEvent> disruptor = null;
		ProducerType producerType = properties.isMultiProducer() ? ProducerType.MULTI : ProducerType.SINGLE;
		disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory, producerType, waitStrategy);
		disruptor.setDefaultExceptionHandler(new EventExceptionHandler(disruptor));

		int consumers = properties.getConsumers();
		DisruptorEventHandler[] eventHandlers = new DisruptorEventHandler[consumers];
		for (int i = 0; i < consumers; i++) {
			// 消费者处理器
			eventHandlers[i] = new DisruptorEventHandler();
		}
		String consumeMode = properties.getConsumeMode();
		if ("P2P".equals(consumeMode)) {// 点对点模式
			disruptor.handleEventsWithWorkerPool(eventHandlers);
		} else {// 发布订阅模式
			disruptor.handleEventsWith(eventHandlers);
		}

		// 启动disruptor线程
		disruptor.start();
		return disruptor;
	}

	@Bean
	public DisruptorTemplate disruptorTemplate() {
		return new DisruptorTemplate();
	}
}
