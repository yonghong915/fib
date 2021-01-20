package com.fib.gateway;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.fib.gateway.netty.disruptor.MessageConsumer;
import com.fib.gateway.netty.disruptor.RingBufferWorkerPoolFactory;
import com.fib.gateway.netty.server.NettyServer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@SpringBootApplication
public class GatewayApplication {
	@Autowired
	private MessageConsumer messageConsumerServer;

	private static MessageConsumer messageConsumertmp;

	@PostConstruct
	public void init() {
		messageConsumertmp = messageConsumerServer;
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(GatewayApplication.class, args);

		MessageConsumer[] conusmers = new MessageConsumer[4];

		for (int i = 0; i < conusmers.length; i++) {
			messageConsumertmp.setConsumerId("code:serverId:" + i);
			conusmers[i] = messageConsumertmp;
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024,
				new BlockingWaitStrategy(), conusmers);

		NettyServer nettyServer = context.getBean(NettyServer.class);
		nettyServer.start(9876);
	}
}
