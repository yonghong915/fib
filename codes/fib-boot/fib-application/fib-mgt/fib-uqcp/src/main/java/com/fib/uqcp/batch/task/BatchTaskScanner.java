package com.fib.uqcp.batch.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.uqcp.batch.IScanner;
import com.fib.uqcp.batch.batchpool.BatchJob;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.batch.taskstep.BatchGenericException;
import com.fib.uqcp.batch.taskstep.GenericServiceException;
import com.fib.uqcp.batch.taskstep.GenericValue;
import com.fib.uqcp.batch.taskstep.TaskStateConstants;
import com.fib.uqcp.service.IDispatcher;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * 批量任务扫描器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-23 11:58:31
 */
public class BatchTaskScanner implements IScanner<BatchJob> {

	private BatchJobPoller jp = null;// 作业调度器

	private IDispatcher dispatcher;// 服务引擎

	protected BatchTaskScanner() {
	}

	public BatchTaskScanner(BatchJobPoller jp) {
		this.jp = jp;
		this.dispatcher = jp.getManager().getDispatcher();
	}

	@Override
	public List<BatchJob> scan() throws BatchGenericException {
		List<BatchJob> returnTaskList = new ArrayList<>();
		//1. 加载待处理任务
		// BatchJobInstance status=TaskStateConstatns.RUNNING_STATUS

		long count = 0;// dispatcher.getDelegator().findCountByCount("BatchJobInstance");
		if (count >= jp.getJobPollerConf().getJobQueueConfig().getTotalSize()) {
			return null;
		}
		Map<String, Object> returnMap = null;
		try {
			returnMap = dispatcher.runSync("loadTodoTask", MapUtil.builder("count", jp.getJobQueue().getExtractCount()).build());
		} catch (GenericServiceException e) {
			throw new BatchGenericException("BTHAOD", null);
		}
		@SuppressWarnings("unchecked")
		List<GenericValue> taskList = MapUtil.get(returnMap, "taskList", List.class);
		
		//2.将扫描到任务状态修改为“准备”
		if (CollUtil.isNotEmpty(taskList)) {
			for (GenericValue task : taskList) {
               DefaultBatchTask batchTask = convertEntity2Task(task,jp);
               batchTask.setState(TaskStateConstants.READY_STATUS);
               returnTaskList.add(batchTask);
			}
			
			Map<String,Object> returnCreateTaskMap = null;
			try {
				Map<String,Object> paramMap = new HashMap<>();
				paramMap.put("taskList", taskList);
				paramMap.put("state", TaskStateConstants.READY_STATUS);
				returnCreateTaskMap = dispatcher.runSync("changeTaskState", paramMap,0,true);
			}catch(GenericServiceException e) {
				throw new BatchGenericException("BTHAOD", null);
			}
			
			if(CollUtil.isEmpty(returnCreateTaskMap)) {
				
			}
		}
		return returnTaskList;
	}

	private DefaultBatchTask convertEntity2Task(GenericValue jobValue, BatchJobPoller jp) {
		Map<String, Object> parameter = null;
		if (null != jobValue.getString("parameter")) {
			parameter = BatchMapSerializer.deserialize(jobValue.getString("parameter"));
		}
		String jobId = jobValue.getString("jobId");
		String jobName = jobValue.getString("jobName");
		DefaultBatchTask batchTask = new DefaultBatchTask(jobId, jobName, jp);

		batchTask.setJobId(jobId);
		batchTask.setJobName(jobName);
		batchTask.setState(jobValue.getString("state"));
		batchTask.setParameter(parameter);
		batchTask.setRefPayNo(jobValue.getString("refPayNo"));
		batchTask.setStartDate(jobValue.getString("createdStamp"));
		batchTask.setHostName(jobValue.getString("hostName"));
		batchTask.setPrioritySwitch(jobValue.getLong("prioritySwitch"));
		batchTask.setOutParamStore(jobValue.getString("outParamStore"));
		batchTask.setJobModelId(jobValue.getString("jobModelId"));
		return batchTask;
	}
}