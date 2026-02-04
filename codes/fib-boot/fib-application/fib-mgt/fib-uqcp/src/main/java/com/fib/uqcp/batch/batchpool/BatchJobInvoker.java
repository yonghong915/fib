package com.fib.uqcp.batch.batchpool;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 作业执行器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 21:17:42
 */
public class BatchJobInvoker implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BatchJobInvoker.class);
	public static final long THREAD_TTL = 18000000;
	public static final int WAIT_TIME = 750;
	private JobInvokerConfig jobInvokerConfig = null;
	protected BatchJobPoller jp = null;
	protected Thread thread = null;
	protected Date created = null;
	protected String name;
	/** 执行的作业的个数 */
	protected int jobRunTimes = 0;
	protected boolean run = false;
	protected BatchJob currentJob = null;
	protected int statusCode = 0;
	protected long jobStart = 0;

	public BatchJobInvoker(BatchJobPoller jp, JobInvokerConfig jobInvokerConfig) {
		this.jobInvokerConfig = jobInvokerConfig;
		this.created = new Date();
		this.run = true;
		this.jobRunTimes = 0;
		this.jp = jp;

		this.thread = new Thread(this);
		this.name = "Batch-iv-" + this.thread.threadId() + "-" + jp.getJobType().name();
		this.thread.setDaemon(false);
		this.thread.setName(this.name);
		this.thread.start();
	}

	protected BatchJobInvoker() {
	}

	public void stop() {
		run = false;

		thread.interrupt();

		try {
			thread.join(30 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void wakUp() {
		notifyAll();
	}

	/**
	 * 获得该线程执行作业的次数
	 * 
	 * @return
	 */
	public int getUsage() {
		return jobRunTimes;
	}

	public long getTimeRemaining() {
		long now = System.currentTimeMillis();
		long time = getTime();
		long ttl = jobInvokerConfig.getThreadTll();
		return (time + ttl) - now;
	}

	/**
	 * 返回该线程被创建时间
	 * 
	 * @return
	 */
	public long getTime() {
		return created.getTime();
	}

	public Date getCreateTime() {
		return created;
	}

	/**
	 * 获取作业开始时间
	 * 
	 * @return
	 */
	public Date getJobStartTime() {
		if (jobStart <= 0) {
			return null;
		}
		return new Date(jobStart);
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 返回线程的运行状态，0空闲，1正在执行作业
	 * 
	 * @return
	 */
	public int getCurrentStatus() {
		return this.statusCode;
	}

	public long getCurrentRuntime() {
		if (this.jobStart > 0) {
			long now = System.currentTimeMillis();
			return now - this.jobStart;
		} else {
			return 0;
		}
	}

	public Long getThreadId() {
		if (this.thread != null) {
			return this.thread.threadId();
		} else {
			return null;
		}
	}

	public String getJobId() {
		if (this.statusCode == 1) {
			if (this.currentJob != null) {
				return this.currentJob.getJobId();
			} else {
				return "WARNING:Invalid Job!";
			}
		} else {
			return null;
		}
	}

	public String getJobName() {
		if (this.currentJob != null) {
			return this.currentJob.getJobName();
		} else {
			return null;
		}
	}

	public void kill() {
		run = false;
		this.statusCode = -1;
		thread.interrupt();
		this.thread = null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				BatchJob job = jp.next();
				if (job != null && job.isValid()) {
					logger.info("Invoker[{}] received jo [{}] from poller [{}]", thread.getName(), job.getJobName(), jp.toString());
				}

				this.currentJob = job;
				this.statusCode = 1;
				this.jobStart = System.currentTimeMillis();

				try {
					job.exec();
				} catch (Exception e) {
					logger.error("", e);
				} catch (Throwable t) {
					logger.error("Failed to execute job [{}]", job.getJobName(), t);
				}
			} finally {
			}
		}

	}
}