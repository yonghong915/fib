package com.fib.commons.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.text.StrFormatter;

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
		if (null == urls || urls.length == 0) {
			throw new IllegalArgumentException("urls must be not null.");
		}
		for (int i = 0; i < urls.length; i++) {
			addURL(urls[i]);
		}
	}

	public void append(String libPath) {
		// 1. lib directory
		File libDir = new File(libPath);
		if (!libDir.exists()) {
			throw new IllegalArgumentException("libPath.notExist");
		}
		if (!libDir.isDirectory()) {
			throw new IllegalArgumentException("libPath.notDirectory");
		}
		if (!libDir.canRead()) {
			throw new IllegalArgumentException("libPath.canNotRead");
		}

		List<URL> jarUrlList = new ArrayList<>(64);

		getAllJarUrl(jarUrlList, libDir);

		// 5. URLClassLoader
		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);
		append(urls);
	}

	public void appendJars(String libPath) {
		if (null == libPath || libPath.isEmpty()) {
			throw new IllegalArgumentException("libPath must not be null!");
		}
		File libDir = new File(libPath);
		if (!libDir.exists()) {
			throw new IllegalArgumentException(StrFormatter.format("libPath {} doesn't exist!", libPath));
		}
		if (!libDir.isDirectory()) {
			throw new IllegalArgumentException(StrFormatter.format("libPath {} is not a directory", libPath));
		}
		if (!libDir.canRead()) {
			throw new IllegalArgumentException(StrFormatter.format("libPath {} can not be read!", libPath));
		}

		List<URL> jarUrlList = new ArrayList<>(64);

		getAllJarUrl(jarUrlList, libDir);

		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);

		append(urls);
	}

	private static void getAllJarUrl(List<URL> jarUrlList, File libDir) {
		String[] jarFiles = libDir.list((dir, name) -> {
			return name.endsWith(".jar");
		});

		File jarFile = null;
		int i = 0;
		try {
			for (i = 0; i < jarFiles.length; i++) {
				jarFile = new File(libDir, jarFiles[i]);
				jarFile = jarFile.getCanonicalFile();
				jarUrlList.add(jarFile.toURI().toURL());
			}
		} catch (Exception e) {
			throw new CommonException("list jar[" + jarFiles[i] + "]", e);
		}
	}
}
