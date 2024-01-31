package com.fib.uqcp.batch.taskstep;

import java.util.HashMap;
import java.util.Map;

import com.fib.uqcp.batch.batchpool.BatchJobPoller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 自定义步骤
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-09 09:51:13
 */
public class UserDefinedTaskStep extends GenericBatchTaskStep {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1666078763960001355L;

	protected UserDefinedTaskStep(String jobId, String jobName, BatchJobPoller jobPoller) {
		super(jobId, jobName, jobPoller);
	}

	@Override
	protected boolean runBusiness() throws BatchBusinessException {
		/* 服务成功标识 */
		boolean isSuccess = false;
		/* 服务名 */
		String serviceName = null;
		Map<String, Object> filterStepParamMap = null;
		Map<String, Object> returnServiceMap = null;
		long start = System.currentTimeMillis();
		try {
			Map<String, Object> baseMap = taskContext.getBaseMap();
			Map<String, Object> extendMap = taskContext.getUserMap();
			Map<String, Object> inputMap = taskContext.getInputMap();
			Map<String, Object> newBaseMap = new HashMap<>();
			newBaseMap.putAll(baseMap);

			Map<String, Object> newInputMap = null;
			if (MapUtil.isNotEmpty(inputMap)) {
				newInputMap = new HashMap<>();
				newInputMap.putAll(inputMap);
			}

			serviceName = (String) inParam.get("serviceName");

		} catch (Exception e) {
		}
		return false;
	}

	@Override
	protected void checkParams() throws BatchGenericException {
		if (MapUtil.isEmpty(inParam) || StrUtil.isEmptyIfStr(inParam.get("serviceName"))) {
			throw new BatchGenericException("E1022", stepName);
		}
	}

}
