package com.fib.pcms.config;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class QuartzManager {
	// private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	private static SchedulerFactoryBean schedulerFactory = null;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	private Scheduler scheduler = null;

	@PostConstruct
	public void init() {
		schedulerFactory = schedulerFactoryBean;
	}

	/**
	 * @Description: 添加一个定时任务
	 *
	 * @param jobName          任务名
	 * @param jobGroupName     任务组名
	 * @param triggerName      触发器名
	 * @param triggerGroupName 触发器组名
	 * @param jobClass         任务
	 * @param cron             时间设置，参考quartz说明文档
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class jobClass, String cron) {
		try {
			// 任务名，任务组，任务执行类
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity(triggerName, triggerGroupName);
			triggerBuilder.startNow();
			// 触发器时间设定
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
			// 创建Trigger对象
			CronTrigger trigger = (CronTrigger) triggerBuilder.build();

			// 调度容器设置JobDetail和Trigger
			scheduler.scheduleJob(jobDetail, trigger);

			// 启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	/**
	 * @Description: 修改一个任务的触发时间
	 *
	 * @param jobName
	 * 
	 * @param jobGroupName
	 * @param triggerName      触发器名
	 * @param triggerGroupName 触发器组名
	 * @param cron             时间设置，参考quartz说明文档
	 */
	public static void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			String cron) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				return;
			}

			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(cron)) {
				System.out.println("任务：" + jobName + "被修改");
				/** 方式一 ：调用 rescheduleJob 开始 */
				/*
				 * // 触发器 TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
				 * // 触发器名,触发器组 triggerBuilder.withIdentity(triggerName, triggerGroupName);
				 * triggerBuilder.startNow(); // 触发器时间设定
				 * triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron)); //
				 * 创建Trigger对象 trigger = (CronTrigger) triggerBuilder.build(); // 方式一
				 * ：修改一个任务的触发时间 scheduler.rescheduleJob(triggerKey, trigger);
				 */
				/** 方式一 ：调用 rescheduleJob 结束 */

				/** 方式二：先删除，然后在创建一个新的Job */
				JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
				Class<? extends Job> jobClass = jobDetail.getJobClass();
				removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
				addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
				/** 方式二 ：先删除，然后在创建一个新的Job */
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务
	 *
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

			scheduler.pauseTrigger(triggerKey);// 停止触发器
			scheduler.unscheduleJob(triggerKey);// 移除触发器
			scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			if (!scheduler.isShutdown()) {
				scheduler.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取当前正在执行的任务
	 * 
	 * @return
	 */
	public static boolean getCurrentJobs(String name) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			List<JobExecutionContext> jobContexts = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext context : jobContexts) {
				if (name.equals(context.getTrigger().getJobKey().getName())) {
					return true;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
