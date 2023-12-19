package com.fib.ctrler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Test {

	public static void main(String[] args) {
		int len = 1000000;
		long start = System.currentTimeMillis();
		BigDecimal total = BigDecimal.ZERO;
		for (int i = 0; i < len; i++) {
			BigDecimal a = new BigDecimal("1.1");
			total = total.add(a);
		}
		total = total.setScale(2, RoundingMode.HALF_UP);
		System.out.println(total.toString());
		long end = System.currentTimeMillis();
		System.out.println("BigDecimal=" + (end - start) + " ms");

		start = System.currentTimeMillis();
		long total1 = 0L;
		for (int i = 0; i < len; i++) {
			long a = 110l;
			total1 += a;
		}
		end = System.currentTimeMillis();
		System.out.println("long=" + (end - start) + " ms");
		
		
		start = System.currentTimeMillis();
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); // 创建DecimalFormat对象，并指定保留2位小数的格式
		System.out.println("long=" + (System.currentTimeMillis() - start) + " ms");
		
		start = System.currentTimeMillis();
		String formattedResult = decimalFormat.format(total1 / 100);
		System.out.println(formattedResult);
		System.out.println("long=" + (System.currentTimeMillis() - start) + " ms");
		
		
		start = System.currentTimeMillis();
		decimalFormat = new DecimalFormat("0.00");
		System.out.println("long=" + (System.currentTimeMillis() - start) + " ms");
	}

}
