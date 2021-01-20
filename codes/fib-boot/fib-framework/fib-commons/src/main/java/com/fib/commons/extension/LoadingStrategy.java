package com.fib.commons.extension;

import com.fib.commons.util.Prioritized;

import cn.hutool.core.util.ArrayUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
public interface LoadingStrategy extends Prioritized {
	String directory();

	default boolean preferExtensionClassLoader() {
		return false;
	}

	default String[] excludedPackages() {
		return ArrayUtil.newArray(String.class, 0);
	}

	default boolean overridden() {
		return false;
	}
}
