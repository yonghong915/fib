package com.fib.commons.extension.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fib.commons.extension.Adaptive;
import com.fib.commons.extension.ExtensionFactory;
import com.fib.commons.extension.ExtensionLoader;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

	private final List<ExtensionFactory> factories;

	public AdaptiveExtensionFactory() {
		ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
		List<ExtensionFactory> list = new ArrayList<>();
		for (String name : loader.getSupportedExtensions()) {
			list.add(loader.getExtension(name));
		}
		factories = Collections.unmodifiableList(list);
	}

	@Override
	public <T> T getExtension(Class<T> type, String name) {
		for (ExtensionFactory factory : factories) {
			T extension = factory.getExtension(type, name);
			if (extension != null) {
				return extension;
			}
		}
		return null;
	}
}