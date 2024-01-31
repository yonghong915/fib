package com.fib.uqcp.batch.taskstep;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fib.uqcp.batch.BatchJobType;
import com.fib.uqcp.batch.BatchTaskContext;
import com.fib.uqcp.batch.batchpool.AbstractBatchJob;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.batch.task.DefaultBatchTask;
import com.fib.uqcp.batch.taskstep.strategy.StepStrategyFactory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 任务步骤抽象类
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 17:29:04
 */
public abstract class GenericBatchTaskStep extends AbstractBatchJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 步骤编号 */
	protected String stepId;

	/** 任务编号 */
	protected String taskId;

	/** 顺序号 */
	protected String stepNum;

	/** 步骤名 */
	protected String stepName;

	/** 状态 */
	protected String state;

	/** 作业编号 */
	protected String stepModeId;

	/** 服务类型 */
	protected String serviceType;

	/** 步骤类型 */
	protected String stepTypeId;

	/** 出参 */
	protected Map<String, Object> outParam;

	/** 入参 */
	protected Map<String, Object> inParam;

	/** 错误信息 */
	protected String message;

	/** 批标识 */
	protected String isBatchStep;

	/** 重试标识 */
	private boolean isRetry = false;

	/** 失败次数 */
	protected int failCount;

	/** 上下文 */
	protected BatchTaskContext taskContext;

	/** 调度器 */
	protected BatchJobPoller jobPoller;

	protected GenericBatchTaskStep(String jobId, String jobName, BatchJobPoller jobPoller) {
		super(jobId, jobName, BatchJobType.STEP_JOB);
		this.jobPoller = jobPoller;
	}

	@Override
	public void exec() {
		long start = System.currentTimeMillis();

		try {
			// 判断任务是否有效
			if (!isValid()) {
				logger.warn("Task[{}] is invalid.Don't continue exec.", this.jobId);
				return;
			}

			// 参数检查
			checkParams();

			taskContext = initTaskContext();

			if (MapUtil.isNotEmpty(inParam)) {
				filterParamExpression(inParam);
			}

			// 设置步骤状态-执行中
			setRunningState();

			boolean isSuccessed = runBusiness();

			// 设置步骤状态-已成功
			if (isSuccessed) {
				setSuccessState();
			}
		} catch (Exception e) {
			logger.error("Failed to ", e);

			try {
				setFailState(e.getMessage());
			} catch (Exception e1) {
				setStepFailState(e.getMessage());
			}
		} finally {
		}
	}

	/**
	 * 过滤表达式-并将表达式进行替换
	 * 
	 * @param inParam2
	 */
	private void filterParamExpression(Map<String, Object> paramMap) {
		if (MapUtil.isEmpty(paramMap)) {
			return;
		}

		Set<String> paramKeySet = paramMap.keySet();
		for (String paramKey : paramKeySet) {
			Object paramValue = paramMap.get(paramKey);
			if ((paramValue instanceof String && UtilExpression.isContainExp((String) paramValue))) {
				paramMap.put(paramKey, UtilExpression.getParam((String) paramValue, taskContext));
			}
		}
	}

	protected void setSuccessState() {
		// 保存上下文
		DefaultBatchTask.storeTaskContext(taskId, taskContext);

		setStepState(TaskStateConstants.SCCUESS_STATUS, null);

		if (isEndStep()) {
			setTaskSuccessState();
		}
	}

	protected void setFailState(String message) {
		this.message = message;
		String strategyType = null;

		setFailCount();

		if (MapUtil.isNotEmpty(inParam)) {
			strategyType = (String) inParam.get(Constants.COMMON_STRATEGY_TYPE);
		}

		// 执行策略
		StepStrategyFactory.createStepStrategy(strategyType, this).handle();
	}

	protected void setRunningState() {
		setStepState(TaskStateConstants.RUNNING_STATUS, null);
	}

	public boolean isEndStep() {
		return BatchStepService.checkEndStep(stepId);
	}

	public void setTaskSuccessState() {
		DefaultBatchTask.setTaskSuccessState(taskId);
	}

	public void setTaskFailState(String message) {
		DefaultBatchTask.setTaskFailState(taskId, message);
	}

	public void setStepFailState(String message) {
		setStepState(TaskStateConstants.FAIL_STATUS, message);
		setTaskFailState(message);
	}

	protected void setFailCount() {
		Map<String, Object> stateContext = new HashMap<>();
		stateContext.put("stepId", stepId);
		stateContext.put("state", TaskStateConstants.FAIL_STATUS);
		stateContext.put("failCount", ++failCount);
		setStepState(stateContext);
	}

	protected abstract boolean runBusiness() throws BatchBusinessException;

	protected abstract void checkParams() throws BatchGenericException;

	public void setRetryState(int delaySeconds) {
		setStepState(TaskStateConstants.INIT_STATUS, delaySeconds);
	}

	public void setWaitState(int delaySeconds) {
		setStepState(TaskStateConstants.WAIT_STATUS, delaySeconds);
	}

	public void setStepState(String step, String message) {
		Map<String, Object> stateContext = new HashMap<>();
		stateContext.put("stepId", stepId);
		stateContext.put("state", state);
		stateContext.put("message", message);
		setStepState(stateContext);
	}

	public void setStepState(Map<String, Object> stateContext) {
		try {
			Map<String, Object> rtnChangeStateMap = runSync("changeTaskStepState", stateContext, 0, true);

			if (!ServiceUtil.isSuccess(rtnChangeStateMap)) {
				logger.error("");
				throw new BatchGenericException(rtnChangeStateMap);
			}
		} catch (GenericServiceException e) {
			logger.error("", e);
			throw new BatchGenericException("ETH0007", "Failt");
		}

	}

	private Map<String, Object> runSync(String string, Map<String, Object> stateContext, int i, boolean b) throws GenericServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStepState(String state, int delaySeconds) {
		Timestamp runTime = DateUtil.offsetSecond(new Date(), delaySeconds).toTimestamp();
		Map<String, Object> stateContext = new HashMap<>();
		stateContext.put("stepId", stepId);
		stateContext.put("state", state);
		stateContext.put("runtime", runTime);

		Map<String, Object> rtnChangeStateMap = null;
		try {
			rtnChangeStateMap = runSync("changeTaskStepState", stateContext, 0, true);
		} catch (GenericServiceException e) {
			logger.error("", e);
			throw new BatchGenericException("ETH0007", "Failt");
		}

		if (!ServiceUtil.isSuccess(rtnChangeStateMap)) {
			logger.error("");
			throw new BatchGenericException(rtnChangeStateMap);
		}
	}

	protected void storeTaskContext() throws BatchGenericException {
		DefaultBatchTask.storeTaskContext(taskId, taskContext);
	}

	public BatchTaskContext initTaskContext() throws BatchGenericException {
		return DefaultBatchTask.getTaskContext(taskId);
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepNum() {
		return stepNum;
	}

	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
}