package com.fib.upp;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.fib.upp.util.DecimalFormatUtil;

public class DecimalFormatTest {

	@Test
	public void test() {
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < 10; i++) {
			service.submit(new Runnable() {
				@Override
				public void run() {
					double rad = Math.random() * 10;
					BigDecimal bd = new BigDecimal(String.valueOf(rad));
					String ss = DecimalFormatUtil.format(bd);
					System.out.println(Thread.currentThread().getName() + " " + rad + "  " + ss);
				}
			});
		}
		
		System.out.println(DecimalFormatUtil.format(new BigDecimal("123445676.0878")));
	}
}
