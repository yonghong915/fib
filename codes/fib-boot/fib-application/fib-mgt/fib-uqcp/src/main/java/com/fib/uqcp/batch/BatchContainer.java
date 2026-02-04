package com.fib.uqcp.batch;

import com.fib.uqcp.batch.batchpool.BatchJobManager;
import com.fib.uqcp.batch.taskstep.BatchGenericException;

/**
 * 批量处理平台加载器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 21:42:25
 */
public class BatchContainer implements Container {

	public void init(String[] args, String configFile) throws ContainerException {
		try {
			BatchJobManager.initInstance();
		} catch (BatchGenericException e) {
			throw new ContainerException("BatchContainer init fail.", e);
		}
	}

	public boolean start() throws ContainerException {
		return true;
	}

	public void stop() throws ContainerException {
		BatchJobManager.getInstance().shutdown();
	}
}
