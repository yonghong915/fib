package com.fib.core.threadpool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 通用线程池属性
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Component
@PropertySource("classpath:config/common/custom_threadpool.properties")
@ConfigurationProperties(prefix = "custom.threadpool")
@Data
public class CustomThreadPoolProperties {
	/** 核心线程数 */
	private int corePoolSize;

	/** 最大线程数 */
	private int maxPoolSize;

	/** 队列大小 */
	private int queueCapacity;

	/** 允许线程空闲时间（单位：默认为秒） */
	private int keepAliveTime;

	/** 线程池中的线程的名称前缀 */
	private String namePrefix;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
}