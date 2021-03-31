package com.fib.pcms.config;

import java.net.InetAddress;

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

	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		String runTaskIP = conditionContext.getEnvironment().getProperty("run.task.ip");
		String localIP = this.getLocalIP();

		log.info("local ip: {} , run Task IP: {}", localIP, runTaskIP);

		return runTaskIP.equals(localIP);
	}

	@SneakyThrows
	public String getLocalIP() {
		InetAddress address = InetAddress.getLocalHost();
		return address.getHostAddress();
	}
}
