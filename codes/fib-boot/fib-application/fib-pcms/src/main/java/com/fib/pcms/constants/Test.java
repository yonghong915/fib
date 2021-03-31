package com.fib.pcms.constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-03-25
 */
public class Test {

	public static void main(String[] args) {
		LocalDate curDay = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		System.out.println(curDay.format(formatter));
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
		System.out.println(LocalDateTime.now().format(formatter));
		int daysOfThisYear = LocalDate.now().lengthOfYear();
		System.out.println("daysOfThisYear=" + daysOfThisYear);
		System.out.println(LocalDate.of(2020, 1, 1).lengthOfYear());

		for (int i = 0; i < 10; i++) {
			new Player().start();
		}
	}

	private static class Player extends Thread {
		@Override
		public void run() {
			System.out.println(getName() + ": " + ThreadLocalRandom.current().nextInt(1, 32));
		}
	}
}
