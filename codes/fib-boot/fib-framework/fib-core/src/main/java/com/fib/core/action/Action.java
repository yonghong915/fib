package com.fib.core.action;

import com.fib.core.context.Context;

public interface Action {

	default void process(Context context) {

	}
}
