package com.fib.commons.extension.factory;

import com.fib.commons.extension.ExtensionFactory;
import com.fib.commons.extension.ExtensionLoader;
import com.fib.commons.extension.SPI;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
public class SpiExtensionFactory implements ExtensionFactory {
	@Override
	public <T> T getExtension(Class<T> type, String name) {
		if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
			ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
			if (!loader.getSupportedExtensions().isEmpty()) {
				return loader.getAdaptiveExtension();
			}
		}
		return null;
	}
}
