package com.fib.uqcp.service;

import java.util.Map;

import com.fib.uqcp.batch.taskstep.GenericServiceException;

public class GenericDispatcher extends AbstractGenericDispatcher {
	protected GenericDispatcher() {
	}

	protected GenericDispatcher(ServiceDispatcher dispatcher) {
		if (this.dispatcher != null) {
			this.dispatcher = dispatcher;
		}

	}

	@Override
	public Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context) throws GenericServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context, int txTimeOut, boolean isNewTx)
			throws GenericServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> runASync(String serviceName, Map<String, ? extends Object> context, int txTimeOut, boolean isNewTx)
			throws GenericServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
