package com.fib.autoconfigure.disruptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.fib.autoconfigure.disruptor.annotation.EventRule;
import com.fib.autoconfigure.disruptor.config.EventHandlerDefinition;
import com.fib.autoconfigure.disruptor.config.Ini;
import com.fib.autoconfigure.disruptor.event.DisruptorEvent;
import com.fib.autoconfigure.disruptor.event.factory.DisruptorBindEventFactory;
import com.fib.autoconfigure.disruptor.event.factory.DisruptorEventThreadFactory;
import com.fib.autoconfigure.disruptor.event.handler.ClearEventHandler;
import com.fib.autoconfigure.disruptor.event.handler.DisruptorEventDispatcher;
import com.fib.autoconfigure.disruptor.event.handler.DisruptorHandler;
import com.fib.autoconfigure.disruptor.event.handler.Nameable;
import com.fib.autoconfigure.disruptor.event.handler.chain.HandlerChainManager;
import com.fib.autoconfigure.disruptor.event.handler.chain.def.DefaultHandlerChainManager;
import com.fib.autoconfigure.disruptor.event.handler.chain.def.PathMatchingHandlerChainResolver;
import com.fib.autoconfigure.disruptor.event.translator.DisruptorEventOneArgTranslator;
import com.fib.autoconfigure.disruptor.util.StringUtils;
import com.fib.autoconfigure.disruptor.util.WaitStrategys;
import com.fib.autoconfigure.util.PrefixUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fib.autoconfigure.disruptor")
@ConditionalOnClass({ Disruptor.class })
@ConditionalOnProperty(prefix = PrefixUtil.DISRUPTOR_PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ DisruptorProperties.class })
public class DisruptorAutoConfiguration implements ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(DisruptorAutoConfiguration.class);
	/**
	 * 处理器链定义
	 */
	private Map<String, String> handlerChainDefinitionMap = new HashMap<>();

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

	/*
	 * Handler实现集合
	 */
	@SuppressWarnings("rawtypes")
	@Bean("disruptorHandlers")
	public Map<String, DisruptorHandler> disruptorHandlers() {
		Map<String, DisruptorHandler> disruptorPreHandlers = new LinkedHashMap<>();

		Map<String, DisruptorHandler> beansOfType = getApplicationContext().getBeansOfType(DisruptorHandler.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, DisruptorHandler>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, DisruptorHandler> entry = ite.next();
				if (entry.getValue() instanceof DisruptorEventDispatcher) {
					// 跳过入口实现类
					continue;
				}

				EventRule annotationType = getApplicationContext().findAnnotationOnBean(entry.getKey(), EventRule.class);
				if (annotationType == null) {
					LOG.error("Not Found AnnotationType {} on Bean {} Whith Name {}", EventRule.class, entry.getValue().getClass(), entry.getKey());
				} else {
					handlerChainDefinitionMap.put(annotationType.value(), entry.getKey());
				}
				disruptorPreHandlers.put(entry.getKey(), entry.getValue());
			}
		}
		return disruptorPreHandlers;
	}

	/*
	 * 处理器链集合
	 */
	@Bean("disruptorEventHandlers")
	public List<DisruptorEventDispatcher> disruptorEventHandlers(DisruptorProperties properties,
			@Qualifier("disruptorHandlers") Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers) {
		// 获取定义 拦截链规则
		List<EventHandlerDefinition> handlerDefinitions = properties.getHandlerDefinitions();
		// 拦截器集合
		List<DisruptorEventDispatcher> disruptorEventHandlers = new ArrayList<>();
		// 未定义，则使用默认规则
		if (CollectionUtils.isEmpty(handlerDefinitions)) {
			EventHandlerDefinition definition = new EventHandlerDefinition();
			definition.setOrder(0);
			definition.setDefinitionMap(handlerChainDefinitionMap);

			// 构造DisruptorEventHandler
			disruptorEventHandlers.add(this.createDisruptorEventHandler(definition, eventHandlers));
		} else {
			// 迭代拦截器规则
			for (EventHandlerDefinition handlerDefinition : handlerDefinitions) {
				// 构造DisruptorEventHandler
				disruptorEventHandlers.add(this.createDisruptorEventHandler(handlerDefinition, eventHandlers));

			}
		}
		// 进行排序
		Collections.sort(disruptorEventHandlers, new OrderComparator());
		return disruptorEventHandlers;
	}

	/*
	 * 构造DisruptorEventHandler
	 */
	protected DisruptorEventDispatcher createDisruptorEventHandler(EventHandlerDefinition handlerDefinition,
			Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers) {

		if (StringUtils.isNotEmpty(handlerDefinition.getDefinitions())) {
			handlerChainDefinitionMap.putAll(this.parseHandlerChainDefinitions(handlerDefinition.getDefinitions()));
		} else if (!CollectionUtils.isEmpty(handlerDefinition.getDefinitionMap())) {
			handlerChainDefinitionMap.putAll(handlerDefinition.getDefinitionMap());
		}

		HandlerChainManager<DisruptorEvent> manager = createHandlerChainManager(eventHandlers, handlerChainDefinitionMap);
		PathMatchingHandlerChainResolver chainResolver = new PathMatchingHandlerChainResolver();
		chainResolver.setHandlerChainManager(manager);
		return new DisruptorEventDispatcher(chainResolver, handlerDefinition.getOrder());
	}

	protected Map<String, String> parseHandlerChainDefinitions(String definitions) {
		Ini ini = new Ini();
		ini.load(definitions);
		Ini.Section section = ini.getSection("urls");
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}
		return section;
	}

	protected HandlerChainManager<DisruptorEvent> createHandlerChainManager(Map<String, DisruptorHandler<DisruptorEvent>> eventHandlers,
			Map<String, String> handlerChainDefinitionMap) {

		HandlerChainManager<DisruptorEvent> manager = new DefaultHandlerChainManager();
		if (!CollectionUtils.isEmpty(eventHandlers)) {
			for (Map.Entry<String, DisruptorHandler<DisruptorEvent>> entry : eventHandlers.entrySet()) {
				String name = entry.getKey();
				DisruptorHandler<DisruptorEvent> handler = entry.getValue();
				if (handler instanceof Nameable nameHandler) {
					nameHandler.setName(name);
				}
				manager.addHandler(name, handler);
			}
		}

		if (!CollectionUtils.isEmpty(handlerChainDefinitionMap)) {
			for (Map.Entry<String, String> entry : handlerChainDefinitionMap.entrySet()) {
				// ant匹配规则
				String rule = entry.getKey();
				String chainDefinition = entry.getValue();
				manager.createChain(rule, chainDefinition);
			}
		}
		return manager;
	}

	@Bean
	@ConditionalOnClass({ Disruptor.class })
	@ConditionalOnProperty(prefix = PrefixUtil.DISRUPTOR_PREFIX, value = "enabled", havingValue = "true")
	public Disruptor<DisruptorEvent> disruptor(DisruptorProperties properties, WaitStrategy waitStrategy, ThreadFactory threadFactory,
			EventFactory<DisruptorEvent> eventFactory, @Qualifier("disruptorEventHandlers") List<DisruptorEventDispatcher> disruptorEventHandlers) {
		Disruptor<DisruptorEvent> disruptor = null;
		ProducerType producerType = properties.isMultiProducer() ? ProducerType.MULTI : ProducerType.SINGLE;
		disruptor = new Disruptor<>(eventFactory, properties.getRingBufferSize(), threadFactory, producerType, waitStrategy);

		if (!ObjectUtils.isEmpty(disruptorEventHandlers)) {
			// 进行排序
			Collections.sort(disruptorEventHandlers, new OrderComparator());
			DisruptorEventDispatcher[] eventHandlers = new DisruptorEventDispatcher[disruptorEventHandlers.size()];
			for (int i = 0; i < disruptorEventHandlers.size(); i++) {
				// 连接消费事件方法，其中EventHandler的是为消费者消费消息的实现类
				DisruptorEventDispatcher eventHandler = disruptorEventHandlers.get(i);
				eventHandlers[i] = eventHandler;
			}
			String consumeMode = properties.getConsumeMode();
			if ("P2P".equals(consumeMode)) {// 点对点模式
				disruptor.handleEventsWithWorkerPool(eventHandlers).then(new ClearEventHandler());
			} else {// 发布订阅模式
				disruptor.handleEventsWith(eventHandlers).then(new ClearEventHandler());
			}
		}

		// 启动disruptor线程
		disruptor.start();

		/**
		 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
		 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
		 */
		// Runtime.getRuntime().addShutdownHook(new DisruptorShutdownHook(disruptor));
		return disruptor;
	}

	@Bean
	public DisruptorTemplate disruptorTemplate() {
		return new DisruptorTemplate();
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
