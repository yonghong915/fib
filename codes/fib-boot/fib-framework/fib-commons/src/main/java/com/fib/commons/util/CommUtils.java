package com.fib.commons.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;

/**
 * 公共工具类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-31
 */
public class CommUtils {
	private CommUtils() {
		// nothing to do
	}

	public static boolean validateLen(String msg, String encoding, int len) {
		try {
			return msg.getBytes(encoding).length > len;
		} catch (UnsupportedEncodingException e) {
			throw new CommonException("message.encoding.unsupport gbk");
		}
	}

	public static String getRandom(int len) {
		String source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder rs = new StringBuilder();
		int sourceLen = source.length();
		for (int i = 0; i < len; i++) {
			int a = ThreadLocalRandom.current().nextInt(sourceLen);
			rs.append(source.charAt(a));
		}
		return rs.toString();
	}

	public static String getRandom(String source, int len) {
		StringBuilder rs = new StringBuilder();
		int sourceLen = source.length();
		for (int i = 0; i < len; i++) {
			int a = ThreadLocalRandom.current().nextInt(sourceLen);
			rs.append(source.charAt(a));
		}
		return rs.toString();
	}

	/**
	 * 是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String str) {
			return str.trim().isEmpty();
		} else if (obj instanceof Collection<?> col) {
			return col.isEmpty();
		} else if (obj instanceof Map<?, ?> map) {
			return map.isEmpty();
		}
		return false;
	}

	/**
	 * 是否不为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static final class NullObject {
		public NullObject() {
			// do nothing
		}

		public String toString() {
			return "ObjectType.NullObject";
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof NullObject;
		}

		@Override
		public int hashCode() {
			return 32;
		}
	}

	/**
	 * 断言对象是否为{@code null} ，如果不为{@code null} ,就执行传入的操作
	 * 
	 * @param value
	 * @param errorSupplier
	 */
	public static void isNull(Object value, Consumer<Object> action) {
		if (null != value) {
			action.accept(value);
		}
	}

	public static void compile(String javaVersion, String encoding, String classRootPath, List<String> javaSrcFiles, String clPath) {
		if (CollUtil.isEmpty(javaSrcFiles)) {
			return;
		}
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		List<String> options = Arrays.asList("-source", javaVersion, "-target", javaVersion, "-encoding", encoding, "-d", classRootPath, "-cp",
				clPath);
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		List<File> files = new ArrayList<>();
		for (String srcFile : javaSrcFiles) {
			files.add(FileUtil.file(srcFile));
		}

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
		Boolean result = compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
		if (result == null || !result) {
			throw new CommonException("Compilation failed.");
		}
	}

	public static String gzip(String source, String charset) {
		byte[] rts = ZipUtil.gzip(source, charset);
		return Base64.encode(rts);
	}

	public static String unzip(String value, String charset) {
		byte[] ret = Base64.decode(value);
		return ZipUtil.unGzip(ret, charset);
	}
}