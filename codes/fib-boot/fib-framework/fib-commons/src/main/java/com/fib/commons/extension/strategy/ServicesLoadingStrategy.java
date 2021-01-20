package com.fib.commons.extension.strategy;

import com.fib.commons.extension.LoadingStrategy;

public class ServicesLoadingStrategy implements LoadingStrategy {

	@Override
	public String directory() {
		return "META-INF/services/";
	}

	@Override
	public boolean overridden() {
		return true;
	}

	@Override
	public int getPriority() {
		return MIN_PRIORITY;
	}

}