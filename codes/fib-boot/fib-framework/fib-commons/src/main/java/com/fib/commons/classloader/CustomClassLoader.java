package com.fib.commons.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * 自定义类加载器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-16
 */
public class CustomClassLoader extends URLClassLoader {
	public CustomClassLoader(ClassLoader classLoader) {
		super(new URL[] {}, classLoader);

	}

	public CustomClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public CustomClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public CustomClassLoader(URL[] urls) {
		super(urls);
	}

	public void append(URL[] urls) {
		if (urls == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < urls.length; i++) {
			addURL(urls[i]);
		}
	}
}
