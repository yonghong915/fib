package com.fib.uqcp.service.monitor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 监控消费者，用于从线程池队列中取出监控任务并执行
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-16 16:05:31
 */
public class MonitorConsumer implements Runnable {

	private BlockingQueue<MonitorMessage> queue;

	public MonitorConsumer(BlockingQueue<MonitorMessage> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
			MonitorMessage msg = queue.poll();
			if (null != msg) {
				MonitorUtil.writeBatchTaskLog(msg);
			}
		} catch (Exception e) {
		}
	}

}
