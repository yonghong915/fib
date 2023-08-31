package com.fib.core.action;

import com.fib.core.context.Context;
import com.fib.core.exception.FibException;

public abstract class JsonFibQueryAction extends FibQueryAction {
	@Override
	public void execute(Context context) throws FibException {
		Object obj = executeInner(context);
		context.setData("json", obj);
	}

	protected abstract Object executeInner(Context context);
}
