package com.fib.uqcp.batch.batchpool;

import com.fib.uqcp.batch.BatchJobType;

/**
 * 批量作业(抽象类)
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 17:25:19
 */
public abstract class AbstractBatchJob implements BatchJob {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3494427964818897562L;
	protected long runtime = -1;
	protected String jobId;
	protected String jobName;
	protected BatchJobType jobType;

	protected AbstractBatchJob() {
		// nothing to do
	}

	protected AbstractBatchJob(String jobId, String jobName, BatchJobType jobType) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.jobType = jobType;
		this.runtime = System.currentTimeMillis();
	}

	@Override
	public abstract void exec();

	@Override
	public String getJobId() {
		return this.jobId;
	}

	@Override
	public String getJobName() {
		return this.jobName;
	}

	@Override
	public long getRuntime() {
		return runtime;
	}

	@Override
	public boolean isValid() {
		return runtime > 0;
	}

	@Override
	public void setValid(boolean isValid) {

	}

	@Override
	public BatchJobType getJobType() {
		return this.jobType;
	}

}
