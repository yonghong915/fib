package com.fib.uqcp.batch.taskstep;

import com.fib.uqcp.batch.batchpool.BatchJobPoller;

public class ExecuteDataTaskStep extends GenericBatchTaskStep {

	protected ExecuteDataTaskStep(String jobId, String jobName, BatchJobPoller jobPoller) {
		super(jobId, jobName, jobPoller);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void checkParams() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean runBusiness() throws BatchBusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
