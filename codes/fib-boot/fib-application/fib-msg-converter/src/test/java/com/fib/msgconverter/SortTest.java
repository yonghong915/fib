package com.fib.msgconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology.LeapYearPatternType;

import com.giantstone.common.util.SortHashMap;

public class SortTest {
	public static void bubbleSort(int[] a) {
		int tmp;
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = 0; j < a.length - 1 - i; j++) {
				if (a[j] > a[j + 1]) {
					tmp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = tmp;
				}
			}
		}
	}

	public static void main(String[] args) {
		int[] arr = { 3, 1, 4, 0, 7, 5 };
		bubbleSort(arr);

		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + "\t");
		}

		System.out.println();
//		DateTime now = new DateTime("2100");
//		boolean isLeap = now.year().isLeap();
//		System.out.println(isLeap);
		int count = 0;
		for (int i = 2000; i < 3000; i++) {
			DateTime now = new DateTime(i + "");
			boolean isLeap = now.year().isLeap();
			if (isLeap) {
				System.out.print(i + "  ");
				count++;
				if (count % 16 == 0) {
					System.out.print("\n");
				}
			}
		}
	}

}
