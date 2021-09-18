package com.fib.msgconverter;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		Map<String, Integer> count = new HashMap<String, Integer>();
		count.put("aa", 11);
		count.put("bb", 22);
		count.put("cc", 33);
		System.out.println(count);
		count.clear();
		System.out.println(count);
	}

}
