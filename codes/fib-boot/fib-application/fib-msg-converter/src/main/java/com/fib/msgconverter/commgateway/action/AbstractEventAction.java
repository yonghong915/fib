package com.fib.msgconverter.commgateway.action;

import org.apache.log4j.Logger;

public abstract class AbstractEventAction {
	protected Logger logger;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public abstract void execute(String commLogId);
}
