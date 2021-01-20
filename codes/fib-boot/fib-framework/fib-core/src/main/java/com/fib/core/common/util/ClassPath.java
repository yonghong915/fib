package com.fib.core.common.util;

import java.io.InputStream;

import com.fib.core.common.URL;
import com.google.zxing.NotFoundException;

public interface ClassPath {
	InputStream openClassfile(String classname) throws NotFoundException;

	/**
	 * Returns the uniform resource locator (URL) of the class file with the
	 * specified name.
	 *
	 * @param classname a fully-qualified class name.
	 * @return null if the specified class file could not be found.
	 */
	URL find(String classname);

	/**
	 * This method is invoked when the <code>ClassPath</code> object is detached
	 * from the search path. It will be an empty method in most of classes.
	 */
	void close();
}
