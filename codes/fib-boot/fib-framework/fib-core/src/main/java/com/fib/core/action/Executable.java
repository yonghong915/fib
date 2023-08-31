package com.fib.core.action;

import com.fib.core.context.Context;
import com.fib.core.exception.FibException;

public interface Executable {
	void execute(Context context) throws FibException;
}
