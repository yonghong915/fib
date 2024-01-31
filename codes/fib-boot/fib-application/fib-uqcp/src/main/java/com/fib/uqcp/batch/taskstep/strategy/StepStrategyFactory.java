package com.fib.uqcp.batch.taskstep.strategy;

import com.fib.uqcp.batch.taskstep.Constants;
import com.fib.uqcp.batch.taskstep.GenericBatchTaskStep;

/**
 * 步骤策略工作
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 19:37:50
 */
public class StepStrategyFactory {
	/**
	 * 创建批量任务步骤
	 * 
	 * @param strategyType
	 * @param taskStep
	 * @return
	 */
	public static IFailStrategy createStepStrategy(String strategyType, GenericBatchTaskStep taskStep) {
		IFailStrategy failStrategy = null;
		if (Constants.RETRY_STRATEGY_TYPE.equalsIgnoreCase(strategyType)) {
			failStrategy = new RetryStrategy(taskStep);
		} else if (Constants.SKIP_STRATEGY_TYPE.equalsIgnoreCase(strategyType)) {
			failStrategy = new SkipStrategy(taskStep);
		} else {
			failStrategy = new StopStrategy(taskStep);
		}
		return failStrategy;
	}
}
