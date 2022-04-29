// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.upp.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.fib.commons.exception.CommonException;

// Referenced classes of package com.giantstone.common.util:
//			ExceptionUtil, StringUtil

public class ClassUtil {

	private static ClassLoader classLoader;

	public ClassUtil() {
	}

	/**
	 * @deprecated Method setClassLoader is deprecated
	 */

	public static void setClassLoader(ClassLoader classloader) {
		classLoader = classloader;
	}

	public static Object newInstance(String s, Object aobj[]) {
		Class class1;
		try {
			class1 = Class.forName(s);
			return newInstance(class1, aobj);
		} catch (ClassNotFoundException e) {
			ExceptionUtil.throwActualException(e);
		}
		return null;
	}

	public static Object newInstance(Class class1, Object aobj[]) {
		Constructor constructor;
		Class aclass[] = getClassArrayByArgumentsArray(aobj);
		try {
			constructor = class1.getConstructor(aclass);
			return constructor.newInstance(aobj);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Exception exception;
//		exception;
//		ExceptionUtil.throwActualException(exception);
		return null;
	}

	private static Class[] getClassArrayByArgumentsArray(Object aobj[]) {
		Class aclass[] = new Class[aobj.length];
		int i = 0;
		for (int j = aobj.length; i < j; i++)
			aclass[i] = aobj[i].getClass();

		return aclass;
	}

	public static Object createClassInstance(String s) {
		Class class1 = null;
		try {
			if (null == classLoader)
				class1 = Class.forName(s);
			else
				class1 = classLoader.loadClass(s);
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			ExceptionUtil.throwActualException(exception);
		}
		return createClassInstance(class1);
	}

	public static Object createClassInstance(Class class1) {
		if (null == class1)
			throw new IllegalArgumentException("parameter.null");
		Object obj = null;
		try {
			obj = class1.newInstance();
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			ExceptionUtil.throwActualException(exception);
		}
		return obj;
	}

	public static Object invoke(Object obj, String s, Class aclass[], Object aobj[]) {
		if (null == obj)
			throw new IllegalArgumentException("parameter.null");
		if (null == s || 0 == s.length())
			throw new IllegalArgumentException("parameter.null");
		Class class1 = obj.getClass();
		Method method = null;
		try {
			method = class1.getMethod(s, aclass);
		} catch (Exception exception) {
			ExceptionUtil.throwActualException(exception);
		}
		Object obj1 = null;
		try {
			obj1 = method.invoke(obj, aobj);
		} catch (Exception exception1) {
			ExceptionUtil.throwActualException(exception1);
		}
		return obj1;
	}

	public static Object getBeanAttributeValue(Object obj, String s) {
		return getBeanAttributeValue(obj, s, null);
	}

	public static Object getBeanAttributeValue(Object obj, String s, String s1) {
		if (null == obj)
			throw new IllegalArgumentException("parameter.null");
		if (null == s || 0 == s.length())
			throw new IllegalArgumentException("parameter.null");
		String s2 = (new StringBuilder()).append("get").append(s.substring(0, 1).toUpperCase()).toString();
		if (s.length() > 1)
			s2 = (new StringBuilder()).append(s2).append(s.substring(1)).toString();
		if (s1 != null)
			s2 = (new StringBuilder()).append(s2).append(s1).toString();
		return invoke(obj, s2, null, null);
	}

	public static void setBeanAttributeValue(Object obj, String s, Object obj1) {
		setBeanAttributeValue(obj, s, ((String) (null)), obj1);
	}

	public static void setBeanAttributeValue(Object obj, String s, Object obj1, Class class1) {
		setBeanAttributeValue(obj, s, null, obj1, class1);
	}

	public static void setBeanAttributeValue(Object obj, String s, String s1, Object obj1) {
		setBeanAttributeValue(obj, s, s1, obj1, null);
	}

	public static void setBeanAttributeValue(Object obj, String s, String s1, Object obj1, Class class1) {
		if (null == obj)
			throw new IllegalArgumentException("parameter.null");
		if (null == s || 0 == s.length())
			throw new IllegalArgumentException("parameter.null");
		String s2 = (new StringBuilder()).append("set").append(s.substring(0, 1).toUpperCase()).toString();
		if (s.length() > 1)
			s2 = (new StringBuilder()).append(s2).append(s.substring(1)).toString();
		if (s1 != null)
			s2 = (new StringBuilder()).append(s2).append(s1).toString();
		if (class1 == null)
			class1 = obj1.getClass();
		invoke(obj, s2, new Class[] { class1 }, new Object[] { obj1 });
	}

	public static void setBeanValueByPath(Object obj, String s, Object obj1, Class class1) {
		int i = s.indexOf('.');
		if (-1 == i) {
			int j = s.indexOf('[');
			int k = s.indexOf(']');
			if (0 < j && 0 < k) {
				List list = (List) getBeanAttributeValue(obj, s.substring(0, j));
				int i1 = Integer.parseInt(s.substring(j + 1, k));
				if (i1 >= list.size()) {
					for (int k1 = list.size(); k1 < i1 + 1; k1++)
						list.add(null);

				}
				list.set(i1, class1);
			} else {
				setBeanAttributeValue(obj, s, obj1, class1);
			}
		} else {
			String s1 = s.substring(0, i);
			Object obj2 = null;
			int l = s1.indexOf('[');
			int j1 = s1.indexOf(']');
			if (0 < l && 0 < j1) {
				int l1 = Integer.parseInt(s1.substring(l + 1, j1));
				String s2 = s1.substring(0, l);
				List list1 = (List) getBeanAttributeValue(obj, s2);
				if (l1 >= list1.size()) {
					for (int i2 = list1.size(); i2 < l1 + 1; i2++)
						invoke(obj, (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(s2)).toString(), null, null);

				}
				obj2 = list1.get(l1);
			} else {
				obj2 = getBeanAttributeValue(obj, s1);
			}
			if (null == obj2)
				throw new RuntimeException("ClassUtil.attribute.null");
			setBeanValueByPath(obj2, s.substring(i + 1), obj1, class1);
		}
	}

	public static void setBeanValueByPath(Object obj, String s, Object obj1) {
		setBeanValueByPath(obj, s, obj1, null);
	}

	public static Object getBeanValueByPath(Object obj, String s) {
		Object obj1 = null;
		int i = s.indexOf('.');
		if (-1 == i) {
			int j = s.indexOf('[');
			int k = s.indexOf(']');
			if (0 < j && 0 < k) {
				int l = Integer.parseInt(s.substring(j + 1, k));
				String s2 = s.substring(0, j);
				List list = (List) getBeanAttributeValue(obj, s2);
				if (l >= list.size()) {
					for (int l1 = list.size(); l1 < l + 1; l1++)
						invoke(obj, (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(s2)).toString(), null, null);

				}
				return list.get(l);
			} else {
				return getBeanAttributeValue(obj, s);
			}
		}
		String s1 = s.substring(0, i);
		Object obj2 = null;
		int i1 = s1.indexOf('[');
		int j1 = s1.indexOf(']');
		if (0 < i1 && 0 < j1) {
			int k1 = Integer.parseInt(s1.substring(i1 + 1, j1));
			String s3 = s1.substring(0, i1);
			List list1 = (List) getBeanAttributeValue(obj, s3);
			if (k1 >= list1.size()) {
				for (int i2 = list1.size(); i2 < k1 + 1; i2++)
					invoke(obj, (new StringBuilder()).append("create").append(StringUtil.toUpperCaseFirstOne(s3)).toString(), null, null);

			}
			obj2 = list1.get(k1);
		} else {
			obj2 = getBeanAttributeValue(obj, s1);
		}
		if (null == obj2)
			throw new RuntimeException("ClassUtil.attribute.null");
		else
			return getBeanValueByPath(obj2, s.substring(i + 1));
	}

	public static Class getBeanFieldClass(Object obj, String s) {
		if (null == obj)
			throw new IllegalArgumentException("parameter.null");
		if (null == s || 0 == s.length())
			throw new IllegalArgumentException("parameter.null");
		s = StringUtil.toLowerCaseFirstOne(s);
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(s);
		} catch (Exception exception) {
			ExceptionUtil.throwActualException(exception);
		}
		return field.getType();
	}

	public static ClassLoader createClassLoader(ClassLoader classloader, String s) {
		File file = new File(s);
		if (!file.exists())
			throw new IllegalArgumentException("ClassUtil.createClassLoader.libPath.notExist");
		if (!file.isDirectory())
			throw new IllegalArgumentException("ClassUtil.createClassLoader.libPath.notDirectory");
		if (!file.canRead())
			throw new IllegalArgumentException("ClassUtil.createClassLoader.libPath.canNotRead");
		List<URL> arraylist = new ArrayList<>(64);
		getAllJarUrl(arraylist, file);
		URL[] aurl = new URL[arraylist.size()];
		arraylist.toArray(aurl);
		for (int i = 0; i < aurl.length; i++)
			System.out.println(aurl[i]);

		URLClassLoader urlclassloader = new URLClassLoader(aurl, classloader);
		return urlclassloader;
	}

	private static void getAllJarUrl(List<URL> list, File file) {
		String[] as = file.list(new FilenameFilter() {

			public boolean accept(File file2, String s) {
				return s.endsWith(".jar");
			}

		});
		int i = 0;
		try {
			for (i = 0; i < as.length; i++) {
				File file1 = new File(file, as[i]);
				file1 = file1.getCanonicalFile();
				list.add(file1.toURI().toURL());
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new CommonException("ClassUtil.getAllJarUrl.listJar.error", exception);
		}
	}
}
