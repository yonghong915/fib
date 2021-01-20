package com.fib.commons.extension.strategy;

import com.fib.commons.extension.LoadingStrategy;

public class FibInternalLoadingStrategy implements LoadingStrategy {

	@Override
	public String directory() {
		return "META-INF/fib/internal/";
	}

	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}

}
