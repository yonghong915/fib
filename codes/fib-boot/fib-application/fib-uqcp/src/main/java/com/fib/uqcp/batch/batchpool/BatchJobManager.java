package com.fib.uqcp.batch.batchpool;


import com.fib.uqcp.batch.task.GenricDelegator;
import com.fib.uqcp.service.IDispatcher;


/**
 * 批量作业管理器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-16 17:25:48
 */
public class BatchJobManager {
	//private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobManager.class);
	//private static final String dispatcherName = "JobDispatcher";
	//private static final String DEFAULT_JOB_POLLER_NAME = "DEFAULT_JOB_POLLER";
	//private Map<String, BatchJobPoller> registeredPollers = new HashMap<>();
	private static BatchJobManager bjm = null;

	private BatchJobManager() {
	}

	public static void initInstance() {
		bjm = new BatchJobManager();
		bjm.init();

	}

	public static BatchJobManager getInstance() {
		if (null == bjm) {
			throw new RuntimeException("");
		}
		return bjm;
	}

	private void init() {
	

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public IDispatcher getDispatcher() {
		// TODO Auto-generated method stub
		return null;
	}

	public GenricDelegator getDelegator() {
		// TODO Auto-generated method stub
		return null;
	}

}
