package com.fib.commons.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-06
 */
public class ClassUtil {
	private static ClassLoader classLoader;

	private ClassUtil() {
	}

	public static void setClassLoader(ClassLoader classloader) {
		classLoader = classloader;
	}

	public static Object createClassInstance(String className) {
		Class<?> clazz = null;
		try {
			if (Objects.isNull(classLoader)) {
				clazz = Class.forName(className);
			} else {
				clazz = classLoader.loadClass(className);
			}
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
		}
		return createClassInstance(clazz);
	}

	public static Object createClassInstance(Class<?> clazz) {
		if (Objects.isNull(clazz)) {
			throw new IllegalArgumentException("parameter.null");
		}
		Object obj = null;
		try {
			obj = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			// ExceptionUtil.throwActualException(exception);
		}
		return obj;
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
						invoke(obj, (new StringBuilder()).append("create").append(StrUtil.upperFirst(s2)).toString(),
								null, null);

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
					invoke(obj, (new StringBuilder()).append("create").append(StrUtil.upperFirst(s3)).toString(), null,
							null);

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
			// ExceptionUtil.throwActualException(exception);
		}
		Object obj1 = null;
		try {
			obj1 = method.invoke(obj, aobj);
		} catch (Exception exception1) {
			// ExceptionUtil.throwActualException(exception1);
		}
		return obj1;
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
						invoke(obj, (new StringBuilder()).append("create").append(StrUtil.upperFirst(s2)).toString(),
								null, null);

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

	public static Class getBeanFieldClass(Object obj, String s) {
		if (null == obj)
			throw new IllegalArgumentException("parameter.null");
		if (null == s || 0 == s.length())
			throw new IllegalArgumentException("parameter.null");
		s = StrUtil.lowerFirst(s);
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(s);
		} catch (Exception exception) {
			 throw new CommonException(exception);
		}
		return field.getType();
	}

	public static <T> T newInstance(String className, Class<T> parentClazz, ClassLoader classLoader) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className, true, classLoader);
			Object obj = clazz.getDeclaredConstructor().newInstance();
			return (T) obj;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new CommonException("aaaa", e);
		}
	}
}
