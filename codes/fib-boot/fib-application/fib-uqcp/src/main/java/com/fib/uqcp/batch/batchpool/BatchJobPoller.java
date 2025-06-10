package com.fib.uqcp.batch.batchpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.uqcp.batch.BatchJobType;
import com.fib.uqcp.batch.IScanner;
import com.fib.uqcp.batch.queue.BatchJobQueue;
import com.fib.uqcp.batch.queue.IQueue;

import cn.hutool.core.collection.CollUtil;

public class BatchJobPoller implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BatchJobPoller.class);
	/** 调度器线程 */
	protected Thread pollerThread = null;

	/** 作业扫描器 */
	protected IScanner<BatchJob> jobScanner = null;

	/** 作业管理器 */
	protected BatchJobManager bjm = null;

	/** 作业队列 */
	protected IQueue jobQueue = null;

	protected BatchJobType jobType = null;

	protected JobPollerConfig jobPollerConfig;

	protected List<BatchJobInvoker> jobInvokerPoll;

	protected volatile boolean isRunning = false;

	protected boolean isDealyFinished = false;

	protected BatchJobPoller() {
	}

	protected BatchJobPoller(BatchJobManager bjm, JobPollerConfig jobPollerConfig, BatchJobType jobType) {
		this.jobPollerConfig = jobPollerConfig;
		this.bjm = bjm;
		this.jobType = jobType;
		this.jobScanner = BatchJobScannerFactory.createJobScanner(jobType, this);
		this.jobQueue = new BatchJobQueue(this, jobPollerConfig.getJobQueueConfig());
		this.jobInvokerPoll = createThreadPool();
		if (this.jobPollerConfig.isEnabled()) {
			startPoll();
		}
	}

	protected void startPoll() {
		if (this.jobPollerConfig.isEnabled()) {
			pollerThread = new Thread(this, this.toString());
			pollerThread.setDaemon(false);
			this.isRunning = true;
			pollerThread.start();
		}
	}

	@Override
	public synchronized void run() {
		delayRun();

		while (isRunning) {
			try {
				List<BatchJob> jobs = jobScanner.scan();
				if (CollUtil.isNotEmpty(jobs)) {
					handleJobs(jobs);
				} else {
					this.wait(this.jobPollerConfig.getWaitTime());
				}
			} catch (Throwable t) {
				logger.error("", t);
				try {
					TimeUnit.MILLISECONDS.sleep(60000 * 10);
				} catch (InterruptedException e) {
					stop();
				}
			}
		}

	}

	private void stop() {
		// TODO Auto-generated method stub

	}

	private void delayRun() {
		// TODO Auto-generated method stub

	}

	private void handleJobs(List<BatchJob> jobs) {
		queueNow(jobs);
	}

	private void queueNow(List<BatchJob> jobs) {
		if (!isRunning) {
			return;
		}

		try {
			synchronized (jobQueue) {
				jobQueue.queueNow(jobs);
			}
		} catch (Throwable t) {
			logger.error("", t);
			return;
		}

		/** 调整作业执行器数量 */
		adjustJobInvoker();
	}

	private void adjustJobInvoker() {
		// TODO Auto-generated method stub

	}

	public BatchJobManager getManager() {
		return bjm;
	}

	public BatchJob next() {
		BatchJob job = null;
		if (isRunning) {
			synchronized (jobQueue) {
				if (jobQueue.getSize() > 0) {
					job = jobQueue.nextJob();
				}
			}
		}
		return job;
	}

	protected List<BatchJobInvoker> createThreadPool() {
		List<BatchJobInvoker> jobInvokerPool = new ArrayList<>();
		JobInvokerConfig jobInvokerConfig = jobPollerConfig.getJobInvokerConfig();
		for (int i = 0; i < jobInvokerConfig.getThreadsCount(); i++) {
			BatchJobInvoker iv = new BatchJobInvoker(this, jobInvokerConfig);
			jobInvokerPool.add(iv);
		}
		return jobInvokerPool;
	}

	public BatchJobType getJobType() {
		// TODO Auto-generated method stub
		return null;
	}

	public BatchJobQueue getJobQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	public JobPollerConfig getJobPollerConf() {
		// TODO Auto-generated method stub
		return null;
	}
}
