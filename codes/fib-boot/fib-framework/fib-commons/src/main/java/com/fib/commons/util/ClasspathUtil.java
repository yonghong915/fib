package com.fib.commons.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public final class ClasspathUtil {
	private static final Logger logger = LoggerFactory.getLogger(ClasspathUtil.class);
	private static List<File> elements = new ArrayList<>();

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
					if (!elements.contains(key)) {
						elements.add(key);
						return true;
					}
				}
			} catch (IOException e) {
				//
			}
		}
		return false;
	}

	public static URL[] getUrls() {
		int cnt = elements.size();
		URL[] urls = new URL[cnt];
		for (int i = 0; i < cnt; i++) {
			try {
				urls[i] = elements.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				// note: this is printing right to the console because at this point we don't
				// have the rest of the system up, not even the logging stuff

			}
		}
		return urls;
	}

	/**
	 * 获取类加载器
	 * 
	 * @return
	 */
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

	/**
	 * 编译类文件
	 * 
	 * @param srcFiles
	 * @param srcRootPath
	 * @param classRootPath
	 * @param encoding
	 */
	public static void compileFiles(List<String> srcFiles, String srcRootPath, String classRootPath, String encoding) {
		if (CollUtil.isEmpty(srcFiles)) {
			logger.warn("modifiedFiles can not be empty.");
			return;
		}
		if (StrUtil.isEmpty(srcRootPath) || StrUtil.isEmpty(classRootPath)) {
			logger.warn("either srcRootPath or classRootPath can not be empty.");
			return;
		}
		encoding = StrUtil.isEmpty(encoding) ? StandardCharsets.UTF_8.name() : encoding;
		File classRoot = new File(classRootPath);
		if (!classRoot.exists()) {
			classRoot.mkdir();
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String[] arg = new String[] { "-encoding", encoding, "-d", classRootPath, "-sourcepath", srcRootPath, "" };

		for (String srcFile : srcFiles) {
			String classFile = classRootPath + srcFile.substring(srcRootPath.length(), srcFile.length() - 5) + ".class";
			if (new File(srcFile).lastModified() < new File(classFile).lastModified()) {
				// 已经编译过，跳过
				logger.info("file {} already compiled.", srcFile);
				continue;
			}
			arg[6] = srcFile;
			logger.info("compiling file :{} ", srcFile);
			int result = compiler.run(null, null, null, arg);
			if (0 == result) {
				logger.info("compiled sucess,file:[{}]", srcFile);
			} else {
				logger.error("compiled fail,file:[{}]", srcFile);
			}
		}
	}
}
