package com.fib.commons.extension.support;

import java.util.Comparator;

import com.fib.commons.extension.Activate;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
public class WrapperComparator implements Comparator<Object> {

	public static final Comparator<Object> COMPARATOR = new WrapperComparator();

	@Override
	public int compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1.equals(o2)) {
			return 0;
		}

		Class<?> clazz1 = (Class<?>) o1;
		Class<?> clazz2 = (Class<?>) o2;

		OrderInfo a1 = parseOrder(clazz1);
		OrderInfo a2 = parseOrder(clazz2);

		int n1 = a1.order;
		int n2 = a2.order;
		// never return 0 even if n1 equals n2, otherwise, o1 and o2 will override each
		// other in collection like HashSet
		return n1 > n2 ? 1 : -1;
	}

	private OrderInfo parseOrder(Class<?> clazz) {
		OrderInfo info = new OrderInfo();
		if (clazz.isAnnotationPresent(Activate.class)) {
			Activate activate = clazz.getAnnotation(Activate.class);
			info.order = activate.order();
		}
		return info;
	}

	private static class OrderInfo {
		private int order;
	}
}