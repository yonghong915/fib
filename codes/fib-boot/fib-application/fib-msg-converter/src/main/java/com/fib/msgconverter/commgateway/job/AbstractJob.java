package com.fib.msgconverter.commgateway.job;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.dao.cronrule.dao.CronRule;
import com.fib.msgconverter.commgateway.dao.cronrule.dao.CronRuleDAO;
import com.fib.msgconverter.commgateway.dao.job.dao.Job;
import com.fib.msgconverter.commgateway.dao.job.dao.JobDAO;
import com.fib.msgconverter.commgateway.dao.joblog.dao.JobLog;
import com.fib.msgconverter.commgateway.dao.joblog.dao.JobLogDAO;
import com.fib.msgconverter.commgateway.job.expression.TimeExpressionParser;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.commgateway.util.serialnumber.SerialNumberGenerator;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 抽象任务
 * 
 * @author 白帆
 * 
 */
public abstract class AbstractJob {
	protected Logger logger;
	protected Job currentJobInfo;
	private Job primitiveJobInfo;
	private Task task;

	protected JobLog currentJobLog = new JobLog();

	/**
	 * 计算任务下次触发时间，如无下次触发时间则返回0
	 * 
	 * @return
	 */
	public long caculateNextFireTime() {
		if (null == currentJobInfo) {
			// throw new RuntimeException(
			// "Current Job is NULL!Caculate Next Fire Time Error!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"AbstractJob.caculateNextFireTime.currentJob.null"));
		}

		Connection conn = null;

		try {
			String scheduleType = currentJobInfo.getScheduleType();
			if (JobConstants.JOB_SCHE_TYP_TIMES_NUM.equals(scheduleType)) {
				// 定次任务
				if (currentJobInfo.getCurrentTimes() == currentJobInfo
						.getMaxTimes()
						&& 0 < currentJobInfo.getMaxTimes()) {
					return 0;
				} else {
					return System.currentTimeMillis()
							+ currentJobInfo.getJobInterval();
				}
			} else if (JobConstants.JOB_SCHE_TYP_TIME.equals(scheduleType)) {
				if (null == conn) {
					conn = ConnectionManager.getInstance().getConnection();
				}
				// 定时任务
				CronRuleDAO cronRuleDAO = new CronRuleDAO();
				cronRuleDAO.setConnection(conn);

				List list = cronRuleDAO.getCronRuleByJobId(currentJobInfo
						.getId(), JobConstants.CRON_RULE_STAT_INVOCATE);
				long fireTime = 0;
				for (int i = 0; i < list.size(); i++) {
					CronRule cronRule = (CronRule) list.get(i);
					long tmp = getTimeByTimeRule(cronRule);
					if (0 == tmp) {
						continue;
					} else {
						if (0 == fireTime) {
							fireTime = tmp;
						} else {
							fireTime = fireTime > tmp ? tmp : fireTime;
						}
					}
				}
				if (null != conn) {
					conn.commit();
				}
				return fireTime;
			} else if (JobConstants.JOB_SCHE_TYP_NUMANDTIME
					.equals(scheduleType)) {
				// 定时定次
				if (currentJobInfo.getCurrentTimes() < currentJobInfo
						.getMaxTimes()
						&& 0 < currentJobInfo.getCurrentTimes()) {
					// 执行次数未达到最大且已开始执行
					return System.currentTimeMillis()
							+ currentJobInfo.getJobInterval();
				} else {
					if (null == conn) {
						conn = ConnectionManager.getInstance().getConnection();
					}
					// 执行次数以达到最大或未开始执行
					CronRuleDAO cronRuleDAO = new CronRuleDAO();
					cronRuleDAO.setConnection(conn);

					List list = cronRuleDAO.getCronRuleByJobId(currentJobInfo
							.getId(), JobConstants.CRON_RULE_STAT_INVOCATE);
					long fireTime = 0;
					for (int i = 0; i < list.size(); i++) {
						CronRule cronRule = (CronRule) list.get(i);
						long tmp = getTimeByTimeRule(cronRule);
						if (0 == tmp) {
							continue;
						} else {
							if (0 == fireTime) {
								fireTime = tmp;
							} else {
								fireTime = fireTime > tmp ? tmp : fireTime;
							}
						}
					}
					if (0 < fireTime && 0 < currentJobInfo.getCurrentTimes()) {
						// 存在下次执行时间并且当前执行次数大于0
						currentJobInfo.setCurrentTimes(0);
						JobDAO jobDAO = new JobDAO();
						jobDAO.setConnection(conn);

						jobDAO.update(currentJobInfo);
					}
					if (null != conn) {
						conn.commit();
					}
					return fireTime;
				}
			} else {
				// throw new RuntimeException("Unkown Schedule Type!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"AbstractJob.caculateNextFireTime.scheduleType.unkown"));
			}
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ee) {
					// ee.printStackTrace();
				}
			}
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		return 0;
	}

	/**
	 * 结束任务，修改任务状态为成功/失败，被结束任务不会再次被调度
	 * 
	 * @param isSuccess
	 */
	public void terminate(boolean isSuccess) {
		if (null == currentJobInfo) {
			// throw new RuntimeException(
			// "Current Job is NULL!Terminate Job Error!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"AbstractJob.terminate.currentJob.null"));
		}

		String state = null;
		if (isSuccess) {
			state = JobConstants.JOB_STAT_SUCCESS;
		} else {
			state = JobConstants.JOB_STAT_FAILED;
		}

		currentJobInfo.setState(state);
		JobDAO jobDAO = new JobDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobDAO.setConnection(conn);
			jobDAO.updateJobState(state, currentJobInfo.getId());
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error(
			// "Terminate Job[" + currentJobInfo.getId() + "] Error!", e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.terminate.error",
					new String[] { currentJobInfo.getId() }), e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 取消任务，修改任务状态为取消，被取消任务不会再次被调度
	 */
	public void cancel() {
		if (null == currentJobInfo) {
			// throw new
			// RuntimeException("Current Job is NULL!Cancel Job Error!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"AbstractJob.cancel.currentJob.null"));
		}
		JobDAO jobDAO = new JobDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobDAO.setConnection(conn);

			jobDAO.updateJobState(JobConstants.JOB_STAT_CANCEL, currentJobInfo
					.getId());
		} catch (Exception e) {
			// e.printStackTrace();
			// logger
			// .error("Cancel Job[" + currentJobInfo.getId() + "] Error!",
			// e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.cancel.error",
					new String[] { currentJobInfo.getId() }), e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行任务，返回任务下次执行时间
	 * 
	 * @return
	 */
	public boolean run() {
		if (null == currentJobInfo) {
			// logger.error("Current Job is NULL!Run Job Error!");
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.run.currentJob.null"));
			// throw new RuntimeException("Current Job is NULL!Run Job Error!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("AbstractJob.run.currentJob.null"));
		}

		currentJobLog.setJobId(currentJobInfo.getId());
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		currentJobLog.setExecuteDate(format.format(date));
		format = new SimpleDateFormat("HHmmss");
		currentJobLog.setExecuteTime(format.format(date));
		String id = null;
		try {
			id = SerialNumberGenerator.getInstance().next("JOB_LOG");
		} catch (Exception e) {
			// logger.error("Get JOB_LOG id Error!" + e.getMessage(), e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.run.getJobLogId.error"), e);
			ExceptionUtil.throwActualException(e);
		}
		while (12 > id.length()) {
			id = "0" + id;
		}
		currentJobLog.setId(currentJobLog.getExecuteDate() + id);

		boolean isSuccess = true;
		try {
			isSuccess = businessLogic();
		} catch (Exception e) {
			isSuccess = false;
			// logger.error("Execute Job's Business Logic Error!", e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.run.businessLogic.execute.error"), e);
			// e.printStackTrace();
		}

		if (isSuccess) {
			currentJobLog.setState(JobConstants.JOB_LOG_STAT_SUCCESS);
		} else {
			currentJobLog.setState(JobConstants.JOB_LOG_STAT_FAILED);
		}

		JobLogDAO jobLogDAO = new JobLogDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobLogDAO.setConnection(conn);

			jobLogDAO.insert(currentJobLog);

			if (JobConstants.JOB_SCHE_TYP_TIMES_NUM.equals(currentJobInfo
					.getScheduleType())
					|| JobConstants.JOB_SCHE_TYP_NUMANDTIME
							.equals(currentJobInfo.getScheduleType())) {
				// 如果是定次任务或定时定次任务，需将当前次数+1
				currentJobInfo
						.setCurrentTimes(currentJobInfo.getCurrentTimes() + 1);

				JobDAO jobDAO = new JobDAO();
				jobDAO.setConnection(conn);

				jobDAO.update(currentJobInfo);

			}
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error("Run Job Error!", e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"AbstractJob.run.runJob.error",
					new String[] { e.getMessage() }), e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}

	private long getTimeByTimeRule(CronRule rule) {
		String expression = rule.getExpression();
		if (null == expression) {
			// throw new RuntimeException("expression is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"AbstractJob.getTimeByTimeRule.expression.null"));
		} else {
			expression = expression.trim();
			if (0 == expression.length()) {
				// throw new RuntimeException("expression is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"AbstractJob.getTimeByTimeRule.expression.blank"));
			}
		}

		TimeExpressionParser expressionParser = null;
		try {
			expressionParser = new TimeExpressionParser(expression);
		} catch (Exception e) {
			// throw new RuntimeException("Parse Expression Error!"
			// + e.getMessage(), e);
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"AbstractJob.getTimeByTimeRule.parseExpression.error",
									new String[] { e.getMessage() }), e);
		}

		return expressionParser.caculateNextFireTimeFromNow();
	}

	public void setCurrentJobInfo(Job currentJobInfo) {
		this.currentJobInfo = currentJobInfo;
		if (null == primitiveJobInfo) {
			primitiveJobInfo = new Job();
			primitiveJobInfo.setAppCategory(currentJobInfo.getAppCategory());
			primitiveJobInfo.setBranchCode(currentJobInfo.getBranchCode());
			primitiveJobInfo.setCurrentTimes(currentJobInfo.getCurrentTimes());
			primitiveJobInfo.setId(currentJobInfo.getId());
			primitiveJobInfo.setJobInterval(currentJobInfo.getJobInterval());
			primitiveJobInfo.setJobClassName(currentJobInfo.getJobClassName());
			primitiveJobInfo.setMaxTimes(currentJobInfo.getMaxTimes());
			primitiveJobInfo.setScheduleType(currentJobInfo.getScheduleType());
			primitiveJobInfo.setStartDate(currentJobInfo.getStartDate());
			primitiveJobInfo.setStartTime(currentJobInfo.getStartTime());
			primitiveJobInfo.setState(currentJobInfo.getState());
			primitiveJobInfo.setType(currentJobInfo.getType());
		}
	}

	public Job getCurrentJobInfo() {
		return currentJobInfo;
	}

	public Job getPrimitiveJobInfo() {
		return primitiveJobInfo;
	}

	protected abstract boolean businessLogic();

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public JobLog getCurrentJobLog() {
		return currentJobLog;
	}

	public void setCurrentJobLog(JobLog currentJobLog) {
		this.currentJobLog = currentJobLog;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}
