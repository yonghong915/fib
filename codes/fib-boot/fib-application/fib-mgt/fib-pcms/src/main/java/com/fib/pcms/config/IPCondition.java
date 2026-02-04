package com.fib.pcms.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 判断是否为本机ip
 *
 * @author 晓风轻
 */
@Slf4j
public class IPCondition implements Condition {
	private Logger logger = LoggerFactory.getLogger(IPCondition.class);

	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		String runTaskIP = conditionContext.getEnvironment().getProperty("run.task.ip");
		String localIP = this.getLocalIP();

		logger.info("local ip: {} , run Task IP: {}", localIP, runTaskIP);

		return runTaskIP.equals(localIP);
	}

	@SneakyThrows
	public String getLocalIP() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address.getHostAddress();
	}
}
