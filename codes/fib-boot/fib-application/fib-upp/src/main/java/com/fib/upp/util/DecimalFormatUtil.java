package com.fib.upp.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * 数字格式化工具类，保留两位小数
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-03-03
 */
public class DecimalFormatUtil {
	private DecimalFormatUtil() {
		// do nothing
	}

	private static final ThreadLocal<DecimalFormat> formaterHolder = ThreadLocal.withInitial(() -> {
		DecimalFormat df = new DecimalFormat("##0.00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		return df;
	});

	public static void unload() {
		formaterHolder.remove();
	}

	public static String format(Object source) {
		String ret = formaterHolder.get().format(source);
		unload();
		return ret;
	}
}
