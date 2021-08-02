package com.fib.upp.service.gateway.module;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public abstract class ModuleEntity {
	/**
	 * 初始化
	 */
	public abstract void initialize();

	/**
	 * 停止
	 */
	public abstract void shutdown();

	@Getter
	@Setter
	private Map<String, String> parameters;
}
