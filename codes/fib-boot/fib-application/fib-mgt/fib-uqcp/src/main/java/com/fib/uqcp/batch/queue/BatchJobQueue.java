package com.fib.uqcp.batch.queue;

import java.util.List;

import com.fib.uqcp.batch.batchpool.BatchJob;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;

public class BatchJobQueue implements IQueue {

	public BatchJobQueue(BatchJobPoller batchJobPoller, Object jobQueueConfig) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void queueNow(List<BatchJob> jobs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BatchJob nextJob() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getExtractCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
