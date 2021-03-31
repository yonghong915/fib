package com.fib.upp;

import org.quartz.core.QuartzSchedulerThread;

public class Test {
	public static void main(String[] args) {
		int[] arr = { 1, 2, 3, 4, 5 };
		System.out.println(arr[0]);
		changeValue(arr);
		System.out.println(arr[0]);
	}

	private static void changeValue(int[] arr) {
		QuartzSchedulerThread a;
		arr[0] = 66;
	}

}
