package com.fib.uqcp.batch.task;

import java.util.Map;

import org.slf4j.Logger;

import com.fib.uqcp.batch.BatchJobType;
import com.fib.uqcp.batch.BatchTaskContext;
import com.fib.uqcp.batch.LogUtil;
import com.fib.uqcp.batch.batchpool.AbstractBatchJob;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.service.IDispatcher;

public class DefaultBatchTask extends AbstractBatchJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6550327664328720118L;
	private static final Logger LOGGER = LogUtil.getLogger(DefaultBatchTask.class);
	/** 流水号 */
	private String refPayNo;
	/** 状态 */
	private String state;
	/** 作业配置编号 */
	private String jobModelId;
	/** 实例编号 */
	private String hostName;
	/** 开始时间 */
	private String startDate;
	/** 优先级 */
	private int prioritySwitch;
	/** 出差开关 */
	private String outParamStore;
	/** 任务入参 */
	private Map<String, Object> parameter;
	/** 服务引擎 */
	private IDispatcher dispatcher;
	/** 实体引擎 */
	private GenricDelegator delegator;
	/** 调度器 */
	private BatchJobPoller jp;
	/** 上下文 */
	private BatchTaskContext taskContext;
	/** 是否有效 */
	private boolean isValid = true;

	public DefaultBatchTask(String jobId, String jobName, BatchJobPoller jp) {
		super(jobId, jobName, BatchJobType.TASK_JOB);
		this.jp = jp;
		this.dispatcher = jp.getManager().getDispatcher();
		this.delegator = jp.getManager().getDelegator();

		taskContext = new BatchTaskContext();
		taskContext.setJobId(jobId);
		this.jobType = BatchJobType.TASK_JOB;
	}

	@Override
	public void exec() {
		// LogUtil.info("Task ready go ! jobId:{}", this.jobId);
	}

	public static void storeTaskContext(String taskId, BatchTaskContext taskContext) {

	}

	public static BatchTaskContext getTaskContext(String taskId) {

		return null;
	}

	public static void setTaskSuccessState(String taskId) {

	}

	public static void setTaskFailState(String taskId, String message) {

	}

	public void setState(String readyStatus) {
		// TODO Auto-generated method stub
		
	}

	public void setJobId(String jobId) {
		// TODO Auto-generated method stub
		
	}

	public void setJobName(String jobName) {
		// TODO Auto-generated method stub
		
	}

	public void setParameter(Map<String, Object> parameter2) {
		// TODO Auto-generated method stub
		
	}

	public void setRefPayNo(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setStartDate(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setHostName(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setOutParamStore(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setPrioritySwitch(Object long1) {
		// TODO Auto-generated method stub
		
	}

	public void setJobModelId(String string) {
		// TODO Auto-generated method stub
		
	}

}
