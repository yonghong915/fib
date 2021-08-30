package com.fib.msgconverter.commgateway.job;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.dao.job.dao.Job;
import com.fib.msgconverter.commgateway.dao.job.dao.JobDAO;
import com.fib.msgconverter.commgateway.job.impl.CommunicateJob;
import com.fib.msgconverter.commgateway.job.impl.LocalJobDelegator;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class JobManager {
	private static Logger logger = null;
	public static ExecutorService EXCUTOR = null;

	private static Timer timer = null;
	private static Map<String, AbstractJob> jobCache = new ConcurrentHashMap<String, AbstractJob>();

	/**
	 * 加载数据库中状态为活动的任务并将他们注册到调度器
	 */
	public static void loadAndScheduleJob() {
		logger = Logger.getLogger("JobManager");
		EXCUTOR = Executors.newCachedThreadPool();
		timer = new Timer("Job Scheduler");

		JobDAO jobDAO = new JobDAO();
		Connection conn = null;
		List list = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobDAO.setConnection(conn);

			list = jobDAO.getJobByState(JobConstants.JOB_STAT_WAIT);
			list.addAll(jobDAO.getJobByState(JobConstants.JOB_STAT_ALIVE));
			if (null != conn) {
				conn.commit();
			}
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ee) {
					// ee.printStackTrace();
				}
			}
			// logger.fatal("Get Job Alive Error!", e);
			logger.fatal(MultiLanguageResourceBundle.getInstance().getString(
					"JobManager.loadAndScheduleJob.getJobAlive.error"), e);
			ExceptionUtil.throwActualException(e);
		}
		for (int i = 0; i < list.size(); i++) {
			Job jobConfig = (Job) list.get(i);
			try {
				startJob(jobConfig, jobDAO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void startJob(Job jobConfig, JobDAO jobDao) {
		AbstractJob job = null;
		if (JobConstants.JOB_TYP_COMMUNICATE.equals(jobConfig.getType())) {
			job = new CommunicateJob();
		} else {
			job = new LocalJobDelegator();
		}
		job.setCurrentJobInfo(jobConfig);
		job.setLogger(logger);

		try {
			addJobSchedule(job);
			if (!JobConstants.JOB_STAT_ALIVE.equals(jobConfig.getType())) {
				jobDao.updateJobState(JobConstants.JOB_STAT_ALIVE, jobConfig
						.getId());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error("Schedule Job[" +
			// job.getCurrentJobInfo().getId()
			// + "] Error!");
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"JobManager.loadAndScheduleJob.scheduleJob.error",
					new String[] { job.getCurrentJobInfo().getId() }));
			job.cancel();
			ExceptionUtil.throwActualException(e);
		}
	}

	/**
	 * 关闭，关闭线程池和调度器
	 */
	public static void shutdown() {
		if (null != EXCUTOR) {
			EXCUTOR.shutdown();
		}
		EXCUTOR = null;

		if (null != timer) {
			timer.cancel();
		}
		timer = null;
	}

	/**
	 * 注册一个任务
	 * 
	 * @param job
	 */
	public static void addJobSchedule(AbstractJob job) {
		if (null == job) {
			// throw new IllegalArgumentException(
			// "Job is NULL!Can not schedule it!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString(
							"JobManager.addJobSchedule.job.null"));
		}
		job.setLogger(logger);

		long nextFireTime = job.caculateNextFireTime();
		if (0 == nextFireTime) {
			return;
		}

		Task task = new Task();
		task.setJob(job);
		task.setTimer(timer);
		try {
			jobCache.put(job.getCurrentJobInfo().getId(), job);
			timer.schedule(task, new Date(nextFireTime));
		} catch (Exception e) {
			// e.printStackTrace();
			task.cancel();
			timer.purge();
			jobCache.remove(job.getCurrentJobInfo().getId());
			ExceptionUtil.throwActualException(e);
		}
	}

	/**
	 * 中断一个任务
	 */
	public static boolean interruptJobSchedule(String jobId) {
		AbstractJob job = jobCache.get(jobId);
		if (null == job) {
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"JobManager.interruptJobSchedule.job.notIn.jobCache",
									new String[] { jobId }));
		}

		boolean result = false;
		try {
			result = job.getTask().cancel();
			job.cancel();
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		return result;
	}
}
