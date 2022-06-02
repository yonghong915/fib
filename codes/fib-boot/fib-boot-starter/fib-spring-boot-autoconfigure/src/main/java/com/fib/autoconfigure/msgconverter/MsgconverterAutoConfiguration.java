package com.fib.autoconfigure.msgconverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.msgconverter.channel.mq.IMQMessageService;
import com.fib.autoconfigure.msgconverter.channel.mq.rocketmq.RocketMQMessageService;
import com.fib.autoconfigure.util.PrefixUtil;

/**
 * 报文转换器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-19 17:14:39
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fib.autoconfigure.msgconverter")
//@ConditionalOnClass({ Disruptor.class })
@ConditionalOnProperty(prefix = PrefixUtil.MSG_CONVERTER_PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ MsgconverterProperties.class })
public class MsgconverterAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgconverterAutoConfiguration.class);

	@Bean("mqMessageService")
	// @ConditionalOnMissingBean(RocketMQTemplate.class)
	public IMQMessageService getMQMessageService() {
		return new RocketMQMessageService();
	}

}
