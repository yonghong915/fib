package com.fib.commons.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 数字格式化，保留两位小数
 * 
 * @author fangyh
 *
 */
public class DecimalFormatUtil {
	private DecimalFormatUtil() {
		// do nothing
	}

	private static final ThreadLocal<NumberFormat> formaterHolder = new ThreadLocal<NumberFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			DecimalFormat df = new DecimalFormat("##0.00");
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(symbols);
			return df;
		}
	};

	public void unload() {
		formaterHolder.remove();
	}

	public static String format(Object source) {
		return formaterHolder.get().format(source);
	}

	public static Number parse(String source) throws ParseException {
		return formaterHolder.get().parse(source);
	}
}
