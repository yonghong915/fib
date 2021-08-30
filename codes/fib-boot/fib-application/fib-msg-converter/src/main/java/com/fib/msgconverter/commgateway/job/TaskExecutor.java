package com.fib.msgconverter.commgateway.job;

import java.util.Timer;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 任务执行器，任务实际上的执行者
 * 
 * @author 白帆
 * 
 */
public class TaskExecutor implements Runnable {
	private Task task;
	private Timer timer;

	public void run() {
		if (null == task) {
			// throw new RuntimeException("Task is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("TaskExecutor.run.task.null"));
		}
		if (null == timer) {
			// throw new RuntimeException("Timer is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("timer.null"));
		}

		boolean isSuccess = true;
		try {
			isSuccess = task.getJob().run();
		} catch (Exception e) {
			// e.printStackTrace();
			isSuccess = false;
			// task.getJob().getLogger().error("Task Run Error!" +
			// e.getMessage(),
			// e);
			task.getJob().getLogger().error(
					MultiLanguageResourceBundle.getInstance().getString(
							"TaskExecutor.run.error",
							new String[] { e.getMessage() }), e);
		}

		// 取消任务
		task.cancel();
		// 删除所有取消任务
		timer.purge();

		if (!(isSuccess && JobConstants.JOB_SCHE_TYP_TIMES_NUM == task.getJob()
				.getCurrentJobInfo().getScheduleType())) {
			// 重新注册任务
			JobManager.addJobSchedule(task.getJob());
		}
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
