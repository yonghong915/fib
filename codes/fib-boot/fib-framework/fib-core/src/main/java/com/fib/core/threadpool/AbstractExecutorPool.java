package com.fib.core.threadpool;

/**
 * 抽象执行线程池
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-03-02 08:20:00
 */
public abstract class AbstractExecutorPool {
	private int cpuNum = Runtime.getRuntime().availableProcessors();

	/** 核心线程数 */
	private int corePoolSize = cpuNum;
	/** 最大线程数 */
	private int maxPoolSize = 2 * cpuNum;;

	/** 允许线程空闲时间（单位：默认为秒） */
	private int keepAliveTime;

	/** 队列大小 */
	private int queueCapacity = 500;

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

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
}
