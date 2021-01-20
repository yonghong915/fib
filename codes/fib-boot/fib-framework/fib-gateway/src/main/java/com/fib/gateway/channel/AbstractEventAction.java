package com.fib.gateway.channel;

import org.slf4j.Logger;

public abstract class AbstractEventAction {
	protected Logger logger;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public abstract void execute(String commLogId);
}
