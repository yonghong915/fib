package com.fib.pcms.config;

import org.quartz.SchedulerException;

/**
 * 调度业务定义规则
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-03-22
 */
public interface IQuartzService {
	/**
	 * 添加一个任务
	 *
	 * @param jobName      job的名称
	 * @param jobGroupName job的组名称
	 * @param jobTime      时间表达式 (这是每隔多少秒为一次任务)
	 * @param runCount     运行的次数 （<0:表示不限次数）
	 */
	public void addJob(String jobName, String jobGroupName, int jobTime, int runCount) throws Exception;

	/**
	 * 增加一个job
	 *
	 * @param jobName      任务名称
	 * @param jobGroupName 任务组名
	 * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
	 */
	public void addJob(String jobName, String jobGroupName, String jobTime) throws Exception;

	/**
	 * 修改一个job任务
	 *
	 * @param jobName      名称
	 * @param jobGroupName 组名
	 * @param jobTime      job的Corn时间
	 */
	public void updateJob(String jobName, String jobGroupName, String jobTime) throws SchedulerException, Exception;

	/**
	 * 删除一个Job任务
	 *
	 * @param jobName      名称
	 * @param jobGroupName 组名称
	 */
	public void deleteJob(String jobName, String jobGroupName) throws SchedulerException, Exception;

	/**
	 * 暂停一个Job任务
	 *
	 * @param jobName      名称
	 * @param jobGroupName 组名
	 */
	public void pauseJob(String jobName, String jobGroupName) throws SchedulerException, Exception;

	/**
	 * 恢复一个任务
	 *
	 * @param jobName      名称
	 * @param jobGroupName 组名
	 */
	public void resumeJob(String jobName, String jobGroupName) throws Exception;

	/**
	 * 立即执行一个任务
	 *
	 * @param jobName      名称
	 * @param jobGroupName 组名
	 */
	public void runAJobNow(String jobName, String jobGroupName) throws Exception;

}
