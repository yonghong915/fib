package com.fib.commons.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClasspathUtil {
	private static List<File> _elements = new ArrayList<>();

	private ClasspathUtil() {
	}

	public ClasspathUtil(String initial) {
		addClasspath(initial);
	}

	public boolean addClasspath(String s) {
		boolean added = false;
		if (s != null) {
			StringTokenizer t = new StringTokenizer(s, File.pathSeparator);
			while (t.hasMoreTokens()) {
				added |= addComponent(t.nextToken());
			}
		}
		return added;
	}

	public boolean addComponent(String component) {
		if ((component != null) && (component.length() > 0)) {
			return addComponent(new File(component));
		}
		return false;
	}

	public boolean addComponent(File component) {
		if (component != null) {
			try {
				if (component.exists()) {
					File key = component.getCanonicalFile();
					if (!_elements.contains(key)) {
						_elements.add(key);
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		return false;
	}

	public static URL[] getUrls() {
		int cnt = _elements.size();
		URL[] urls = new URL[cnt];
		for (int i = 0; i < cnt; i++) {
			try {
				urls[i] = _elements.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				// note: this is printing right to the console because at this point we don't
				// have the rest of the system up, not even the logging stuff
				System.out.println("Error adding classpath entry: " + e.toString());
				e.printStackTrace();
			}
		}
		return urls;
	}

	public static ClassLoader getClassLoader() {
		URL[] urls = getUrls();

		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = ClasspathUtil.class.getClassLoader();
		}
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}
		return new URLClassLoader(urls, parent);
	}
}
