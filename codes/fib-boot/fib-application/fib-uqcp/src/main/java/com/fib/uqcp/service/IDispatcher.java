package com.fib.uqcp.service;

import java.util.Map;

import com.fib.uqcp.batch.taskstep.GenericServiceException;

/**
 * Generic Services Dispatcher
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-11 10:17:11
 */
public interface IDispatcher {

	Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context) throws GenericServiceException;

	Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context, int txTimeOut, boolean isNewTx)
			throws GenericServiceException;

	Map<String, Object> runASync(String serviceName, Map<String, ? extends Object> context, int txTimeOut, boolean isNewTx)
			throws GenericServiceException;
}
