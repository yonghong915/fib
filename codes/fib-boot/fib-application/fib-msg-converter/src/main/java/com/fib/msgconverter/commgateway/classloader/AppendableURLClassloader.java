package com.fib.msgconverter.commgateway.classloader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

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
			// throw new IllegalArgumentException("urls is null!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("inputParameter.null", new String[] { "urls" }));
		}
		for (int i = 0; i < urls.length; i++) {
			addURL(urls[i]);
		}
	}

	public void append(String libPath) {
		// 1. lib directory
		File libDir = new File(libPath);
		if (!libDir.exists()) {
			// throw new IllegalArgumentException("libPath[" + libPath
			// + "] doesn't exist!");
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("libPath.notExist", new String[] { libPath }));
		}
		if (!libDir.isDirectory()) {
			// throw new IllegalArgumentException("libPath[" + libPath
			// + "] is not a directory!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("libPath.notDirectory", new String[] { libPath }));
		}
		if (!libDir.canRead()) {
			// throw new IllegalArgumentException("libPath[" + libPath
			// + "] can not be read!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("libPath.canNotRead",
					new String[] { libPath }));
		}

		List jarUrlList = new ArrayList(64);

		getAllJarUrl(jarUrlList, libDir);

		// 5. URLClassLoader
		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);
		append(urls);
	}

	private static void getAllJarUrl(List jarUrlList, File libDir) {
		String[] jarFiles = libDir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});
		File jarFile = null;
		int i = 0;
		try {
			for (i = 0; i < jarFiles.length; i++) {
				jarFile = new File(libDir, jarFiles[i]);
				jarFile = jarFile.getCanonicalFile();
				jarUrlList.add(jarFile.toURL());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// throw new RuntimeException("list jar[" + jarFiles[i] + "]", e);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"AppendableURLClassloader.getAllJarUrl.listJar.error",
					new String[] { jarFiles[i], e.getMessage() }));
		}
	}

}
