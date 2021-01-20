package com.fib.commons.extension.strategy;

import com.fib.commons.extension.LoadingStrategy;

public class FibLoadingStrategy implements LoadingStrategy {

	@Override
	public String directory() {
		return "META-INF/fib/";
	}

	@Override
	public boolean overridden() {
		return true;
	}

	@Override
	public int getPriority() {
		return NORMAL_PRIORITY;
	}

}
