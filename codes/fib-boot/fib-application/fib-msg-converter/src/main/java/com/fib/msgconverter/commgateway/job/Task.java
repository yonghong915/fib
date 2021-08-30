package com.fib.msgconverter.commgateway.job;

import java.util.Timer;
import java.util.TimerTask;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 任务，调度器调度的原子单位
 * 
 * @author 白帆
 * 
 */
public class Task extends TimerTask {
	private AbstractJob job;
	private Timer timer;

	public void run() {
		if (null == job) {
			// throw new RuntimeException("Job is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("Task.run.job.null"));
		}
		if (null == timer) {
			// throw new RuntimeException("Timer is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("timer.null"));
		}

		TaskExecutor executor = new TaskExecutor();
		executor.setTask(this);
		executor.setTimer(timer);

		// executor.start();
		JobManager.EXCUTOR.execute(executor);
	}

	public AbstractJob getJob() {
		return job;
	}

	public void setJob(AbstractJob job) {
		this.job = job;
		job.setTask(this);
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
