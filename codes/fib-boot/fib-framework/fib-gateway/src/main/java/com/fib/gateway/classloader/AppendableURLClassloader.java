package com.fib.gateway.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-05
 */
public class AppendableURLClassloader extends URLClassLoader {

	public AppendableURLClassloader(ClassLoader classLoader) {
		super(new URL[] {}, classLoader);

	}

	public AppendableURLClassloader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public AppendableURLClassloader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public AppendableURLClassloader(URL[] urls) {
		super(urls);
	}

	public void append(URL[] urls) {
		if (urls == null) {
			throw new IllegalArgumentException("urls is null!");
		}
		for (int i = 0; i < urls.length; i++) {
			addURL(urls[i]);
		}
	}

}
