package com.fib.pcms.config;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

/**
 * 调度任务实现类
 *
 * @author : liukx
 * @date : 2019/5/21 - 16:29
 */
//@Service("quartzServiceImpl")
//@Conditional(IPCondition.class)
public class QuartzServiceImpl implements IQuartzService {

	private Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

	@Qualifier("Scheduler")
	@Autowired
	private Scheduler scheduler;

	@PostConstruct
	public void startScheduler() {
		try {
			scheduler.start();
			logger.info(" 调度器启动了 ...");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addJob(String jobName, String jobGroupName, int jobTime, int runCount) throws Exception {
		logger.info(" 新增一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=" + jobTime + ","
				+ "runCount=" + runCount);
		Date date = JobUtils.addJob(scheduler, DefaultQuartzJobBean.class, jobName, jobGroupName, jobTime, runCount);
		logger.info(" job任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + " 执行完成 . " + date);
	}

	@Override
	public void addJob(String jobName, String jobGroupName, String jobTime) throws Exception {
		logger.info(" 新增一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=" + jobTime);
		Date date = JobUtils.addJob(scheduler, DefaultQuartzJobBean.class, jobName, jobGroupName, jobTime);
		logger.info(" job任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + " 执行完成 . " + date);
	}

	/**
	 * 修改 一个job的 时间表达式
	 *
	 * @param jobName
	 * @param jobGroupName
	 * @param jobTime
	 */
	@Override
	public void updateJob(String jobName, String jobGroupName, String jobTime) throws Exception {
		logger.info(" 修改一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=" + jobTime);
		Date date = JobUtils.updateJob(scheduler, jobName, jobGroupName, jobTime);
		logger.info(" job任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + " 执行完成 . ");
	}

	/**
	 * 删除任务一个job
	 *
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 */
	@Override
	public void deleteJob(String jobName, String jobGroupName) throws Exception {
		logger.info(" 删除一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=");
		boolean result = JobUtils.deleteJob(scheduler, jobName, jobGroupName);
		logger.info(" job任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + " 执行完成." + result);
	}

	/**
	 * 暂停一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	@Override
	public void pauseJob(String jobName, String jobGroupName) throws Exception {
		logger.info(" 暂停一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=");
		JobUtils.pauseJob(scheduler, jobName, jobGroupName);
		logger.info(" job任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + " 执行完成.");
	}

	/**
	 * 恢复一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	@Override
	public void resumeJob(String jobName, String jobGroupName) throws Exception {
		logger.info(" 恢复一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=");
		JobUtils.resumeJob(scheduler, jobName, jobGroupName);
		logger.info(" 恢复一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + "执行完成");
	}

	/**
	 * 立即执行一个job
	 *
	 * @param jobName
	 * @param jobGroupName
	 */
	@Override
	public void runAJobNow(String jobName, String jobGroupName) throws Exception {
		logger.info(" 立即执行一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + ",jobTime=");
		JobUtils.runAJobNow(scheduler, jobName, jobGroupName);
		logger.info(" 立即执行一个任务 jobName=" + jobName + ",jobGroupName" + jobGroupName + "完成 ...");

	}
}
