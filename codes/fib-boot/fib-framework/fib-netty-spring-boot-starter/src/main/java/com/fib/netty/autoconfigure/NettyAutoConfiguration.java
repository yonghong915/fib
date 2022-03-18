package com.fib.netty.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fib.netty.client.NettyClient;
import com.fib.netty.server.NettyServer;

@Configuration
@ConditionalOnClass({ NettyServer.class, NettyClient.class })
//@EnableConfigurationProperties(SmsProperties.class)
@ComponentScan("com.fib.netty")
public class NettyAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(NettyServer.class)
	// @ConditionalOnProperty(prefix = "sms", value = "enabled", havingValue =
	// "true")
	public NettyServer nettyServer() {
		return new NettyServer();
	}

	@Bean
	@ConditionalOnMissingBean(NettyClient.class)
	// @ConditionalOnProperty(prefix = "sms", value = "enabled", havingValue =
	// "true")
	public NettyClient nettyClient() {
		return new NettyClient();
	}
}
