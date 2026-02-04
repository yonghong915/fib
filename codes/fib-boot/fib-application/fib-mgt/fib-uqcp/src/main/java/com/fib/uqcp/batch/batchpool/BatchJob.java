package com.fib.uqcp.batch.batchpool;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.uqcp.batch.BatchJobType;

/**
 * 批量作业(接口)
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 17:16:42
 */
public interface BatchJob extends Serializable {
	public Logger logger = LoggerFactory.getLogger(BatchJob.class);

	/**
	 * 执行作业任务
	 */
	public void exec();

	/**
	 * 返回作业ID
	 * 
	 * @return
	 */
	public String getJobId();

	/**
	 * 返回作业名称
	 * 
	 * @return
	 */
	public String getJobName();

	/**
	 * 返回运行时间
	 * 
	 * @return
	 */
	public long getRuntime();

	/**
	 * 是否有效标志
	 * 
	 * @return
	 */
	public boolean isValid();

	public void setValid(boolean isValid);

	/**
	 * 获取作业类型
	 * 
	 * @return
	 */
	public BatchJobType getJobType();

}
