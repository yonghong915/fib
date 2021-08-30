package com.fib.msgconverter.commgateway.module.impl;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.job.JobManager;
import com.fib.msgconverter.commgateway.module.Module;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

public class JobScheduleManager extends Module {

	public void initialize() {
		if (!CommGateway.isDatabaseSupport()) {
			// throw new RuntimeException(
			// "You need turn on Database Support first!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"JobScheduleManager.initialize.noDatabase"));
		}
		JobManager.loadAndScheduleJob();
		CommGateway.setJobSupport(true);
	}

	public void shutdown() {
		JobManager.shutdown();
	}

}
