package com.fib.oms;

import java.net.URLConnection;

public class OmsApplication {
	public static void main(String[] args) {
		String str = "a|b|c|||||";
		String[] a = str.split("\\|",5);
		System.out.println(a.length);
	}
}
