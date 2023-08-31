package com.fib.core.action;

import com.fib.core.context.Context;
import com.fib.core.exception.FibException;

public class FibQueryAction implements Action, Executable {

	public void prepare(Context context) {
		// nothing to do
	}

	@Override
	public void execute(Context context) throws FibException {
		prepare(context);
		process(context);
	}
}