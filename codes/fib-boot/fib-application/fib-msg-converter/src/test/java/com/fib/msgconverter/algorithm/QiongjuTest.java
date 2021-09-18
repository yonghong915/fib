package com.fib.msgconverter.algorithm;

public class QiongjuTest {
	static int chichen, habbit;

	public static int qiongJu(int head, int foot) {
		int re, i, j;
		re = 0;
		for (i = 0; i <= head; i++) {
			j = head - i;
			if (i * 2 + j * 4 == foot) {
				re = 1;
				chichen = i;
				habbit = j;
			}
		}
		return re;
	}

	public static void main(String[] args) {
		int head = 35;
		int foot = 94;
		int re = qiongJu(head, foot);
		System.out.println(re);
		if (re == 1) {
			System.out.println(chichen);
			System.out.println(habbit);
		}
	}
}