package com.fib.pcms.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 调度任务工具类
 *
 * @author ： liukx
 * @time ： 2019/5/21 - 16:35
 */
public class JobUtils {

	/**
	 * 增加一个job
	 *
	 * @param jobClass     任务实现类
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 * @param jobTime      时间表达式 (这是每隔多少秒为一次任务)
	 * @param jobTimes     运行的次数 （<0:表示不限次数）
	 */
	public static Date addJob(Scheduler scheduler, Class<? extends QuartzJobBean> jobClass, String jobName,
			String jobGroupName, int jobTime, int jobTimes) throws Exception {
		// 任务名称和组构成任务key
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
		// 使用simpleTrigger规则
		Trigger trigger = null;
		if (jobTimes < 0) {
			trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
					.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
					.startNow().build();
		} else {
			trigger = TriggerBuilder
					.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(SimpleScheduleBuilder
							.repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
					.startNow().build();
		}
		return scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 增加一个job
	 *
	 * @param jobClass     任务实现类
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
	 */
	public static Date addJob(Scheduler scheduler, Class<? extends QuartzJobBean> jobClass, String jobName,
			String jobGroupName, String jobTime) throws Exception {
		// 创建jobDetail实例，绑定Job实现类
		// 指明job的名称，所在组的名称，以及绑定job类
		// 任务名称和组构成任务key
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
		// 定义调度触发规则
		// 使用cornTrigger规则
		// 触发器key
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
				.startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
				.withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).startNow().build();
		// 把作业和触发器注册到任务调度中
		return scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 修改 一个job的 时间表达式
	 *
	 * @param jobName
	 * @param jobGroupName
	 * @param jobTime
	 */
	public static Date updateJob(Scheduler scheduler, String jobName, String jobGroupName, String jobTime)
			throws SchedulerException {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
		// 重启触发器
		return scheduler.rescheduleJob(triggerKey, trigger);
	}

	/**
	 * 删除任务一个job
	 *
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 */

	public static boolean deleteJob(Scheduler scheduler, String jobName, String jobGroupName)
			throws SchedulerException {
		return scheduler.deleteJob(new JobKey(jobName, jobGroupName));
	}

	/**
	 * 暂停一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroupName) throws Exception {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroupName) throws Exception {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 立即执行一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	public static void runAJobNow(Scheduler scheduler, String jobName, String jobGroupName) throws Exception {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		scheduler.triggerJob(jobKey);
	}

	/**
	 * 获取所有计划中的任务列表
	 *
	 * @return
	 */
	public static List<Map<String, Object>> queryAllJob(Scheduler scheduler) {
		List<Map<String, Object>> jobList = null;
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			jobList = new ArrayList<Map<String, Object>>();
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					Map<String, Object> map = new HashMap<>();
					map.put("jobName", jobKey.getName());
					map.put("jobGroupName", jobKey.getGroup());
					map.put("description", "触发器:" + trigger.getKey());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					map.put("jobStatus", triggerState.name());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						map.put("jobTime", cronExpression);
					}
					jobList.add(map);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}

	/**
	 * 获取所有正在运行的job
	 *
	 * @return
	 */
	public static List<Map<String, Object>> queryRunJob(Scheduler scheduler) {
		List<Map<String, Object>> jobList = null;
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				Map<String, Object> map = new HashMap<String, Object>();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				map.put("jobName", jobKey.getName());
				map.put("jobGroupName", jobKey.getGroup());
				map.put("description", "触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				map.put("jobStatus", triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					map.put("jobTime", cronExpression);
				}
				jobList.add(map);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return jobList;
	}
}
