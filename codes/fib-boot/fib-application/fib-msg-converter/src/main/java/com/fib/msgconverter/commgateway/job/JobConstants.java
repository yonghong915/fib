package com.fib.msgconverter.commgateway.job;

/**
 * 任务管理相关常量
 * 
 * @author 白帆
 * 
 */
public class JobConstants {
	// 任务表-类型 通讯任务
	public static final String JOB_TYP_COMMUNICATE = "00";
	// 任务表-类型 本地任务
	public static final String JOB_TYP_LOCAL = "01";
	// 任务表-类型 待执行
	public static final String JOB_STAT_WAIT = "00";
	// 任务表-状态 活动
	public static final String JOB_STAT_ALIVE = "01";
	// 任务表-状态 成功结束
	public static final String JOB_STAT_SUCCESS = "02";
	// 任务表-状态 失败结束
	public static final String JOB_STAT_FAILED = "03";
	// 任务表-状态 取消
	public static final String JOB_STAT_CANCEL = "04";
	// 任务表-调度类型 定次
	public static final String JOB_SCHE_TYP_TIMES_NUM = "00";
	// 任务表-调度类型 定时
	public static final String JOB_SCHE_TYP_TIME = "01";
	// 任务表-调度类型 定时定次
	public static final String JOB_SCHE_TYP_NUMANDTIME = "02";

	// 任务计时调度规则表-状态 启用
	public static final String CRON_RULE_STAT_INVOCATE = "00";
	// 任务计时调度规则表-状态 取消
	public static final String CRON_RULE_STAT_CANCEL = "01";

	// 任务执行日志表-状态 成功
	public static final String JOB_LOG_STAT_SUCCESS = "00";
	// 任务执行日志表-状态 失败
	public static final String JOB_LOG_STAT_FAILED = "01";

}
