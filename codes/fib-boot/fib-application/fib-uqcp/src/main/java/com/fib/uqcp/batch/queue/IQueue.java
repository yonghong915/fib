package com.fib.uqcp.batch.queue;

import java.util.List;

import com.fib.uqcp.batch.batchpool.BatchJob;

public interface IQueue {
	void queueNow(List<BatchJob> jobs);

	int getSize();

	BatchJob nextJob();

}
