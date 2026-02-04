package com.fib.uqcp.batch.taskstep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.uqcp.batch.IScanner;
import com.fib.uqcp.batch.batchpool.BatchJob;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.service.IDispatcher;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

public class BatchStepScanner implements IScanner<BatchJob> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchStepScanner.class);

	private BatchJobPoller jp;

	@Override
	public List<BatchJob> scan() throws BatchGenericException {
		List<BatchJob> returnStepList = new ArrayList<>();
		IDispatcher dispatcher = null;
		Map<String, Object> returnLoadMap = null;

//		try {
//			//returnLoadMap = dispatcher.runSync("loadTodoStep", MapBuilder.create().put("count", jp.getJobQueue().getExt()));
//		} catch (GenericServiceException e) {
//			LOGGER.error("aaaaa", e);
//			throw new BatchGenericException("E0007", "loadTodoStep", e.getMessage());
//		}

		if (!ServiceUtil.isSuccess(returnLoadMap)) {
			LOGGER.error("aaaa");
			throw new BatchGenericException(returnLoadMap);
		}

		@SuppressWarnings("unchecked")
		List<GenericValue> stepList = MapUtil.get(returnLoadMap, "stepList", List.class);
		if (CollUtil.isNotEmpty(stepList)) {
			for (GenericValue step : stepList) {
				GenericBatchTaskStep taskStep = null;
				try {
					taskStep = convertStepEntity2TaskStep(step, jp);
					taskStep.setStepState(TaskStateConstants.READY_STATUS, null);
					returnStepList.add(taskStep);
				} catch (Exception e) {
					LOGGER.error("", e);
					if (null != taskStep) {
						taskStep.setFailState(e.getMessage());
					}
				}
			}
		}
		return returnStepList;
	}

	private GenericBatchTaskStep convertStepEntity2TaskStep(GenericValue stepEntity, BatchJobPoller jp) {
		// TODO Auto-generated method stub
		return null;
	}

}
