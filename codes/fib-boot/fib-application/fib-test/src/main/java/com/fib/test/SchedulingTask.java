package com.fib.test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public class SchedulingTask {
	private static ThreadPoolTaskScheduler threadPoolTaskScheduler;
	static {
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("MySchedulingTask-");
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.initialize();
	}

	private Runnable task;

	private ScheduledFuture<?> future;

	private String cronStr = "*/10 * * * * *";

	// 定时任务
	public SchedulingTask() {
	}

	public SchedulingTask(Runnable task) {
		this.task = task;
	}

	public SchedulingTask(Runnable task, String cronStr) {
		this.task = task;
		this.cronStr = cronStr;
	}

	// 启动定时任务
	public ScheduledFuture<?> startCron() {
		future = threadPoolTaskScheduler.schedule(task, new Trigger() {
			@Override
			public Instant nextExecution(TriggerContext triggerContext) {
				return new CronTrigger(cronStr).nextExecution(triggerContext);
			}
		});
		return future;
	}

	// 取消定时任务
	public void stopCron() {
		if (future != null) {
			future.cancel(true);
		}
	}

	// 修改定时任务
	public void changeCron(String cronStr) {
		this.cronStr = cronStr;
		stopCron();
		startCron();
	}

	// getter setter
	public Runnable getTask() {
		return task;
	}

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public String getCronStr() {
		return cronStr;
	}

	public void setTask(Runnable task) {
		this.task = task;
	}

	public void setCronStr(String cronStr) {
		this.cronStr = cronStr;
	}

	public static void setThreadPoolTaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
		SchedulingTask.threadPoolTaskScheduler = threadPoolTaskScheduler;
	}

	public static void main(String[] args) {
		SchedulingTask task = new SchedulingTask();
		task.setTask(() -> {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
			System.out.println("running at " + format.format(new Date()));
		});

		task.startCron();
	}
	// 测试
//	    public class MyTask implements Runnable {
	//
//	        @Override
//	        public void run() {
//	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
//	            System.out.println("running at " + format.format(new Date()));
//	        }
//	    }
}
