package com.fib.core.context;

import java.util.HashMap;
import java.util.Map;

public class ContextImpl extends AbstractContext {
	public ContextImpl() {
		super(null);
	}

	public ContextImpl(String key) {
		super(key);
	}

	@Override
	public Map<String, Object> getDataMap() {
		return new HashMap<>(super.getDataMap());
	}
}