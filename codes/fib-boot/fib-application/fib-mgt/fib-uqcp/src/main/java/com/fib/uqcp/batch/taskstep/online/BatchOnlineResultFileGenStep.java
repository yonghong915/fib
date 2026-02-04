package com.fib.uqcp.batch.taskstep.online;

import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.batch.taskstep.BatchBusinessException;
import com.fib.uqcp.batch.taskstep.GenericBatchTaskStep;

public class BatchOnlineResultFileGenStep extends GenericBatchTaskStep {

	protected BatchOnlineResultFileGenStep(String jobId, String jobName, BatchJobPoller jobPoller) {
		super(jobId, jobName, jobPoller);
		// TODO Auto-generated constructor stub
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
