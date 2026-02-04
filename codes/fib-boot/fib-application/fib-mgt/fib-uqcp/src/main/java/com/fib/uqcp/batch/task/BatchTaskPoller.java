package com.fib.uqcp.batch.task;

import com.fib.uqcp.batch.BatchJobType;
import com.fib.uqcp.batch.batchpool.BatchJobManager;
import com.fib.uqcp.batch.batchpool.BatchJobPoller;
import com.fib.uqcp.batch.batchpool.JobPollerConfig;

/**
 * 作业调度器（该类负责轮询扫描作业，将扫描到作业执行业务处理）
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-23 11:53:23
 */
public class BatchTaskPoller extends BatchJobPoller {
	public BatchTaskPoller(BatchJobManager bjm, JobPollerConfig jobPollerConfig, BatchJobType jobType) {
		super(bjm, jobPollerConfig, jobType);
	}
}
