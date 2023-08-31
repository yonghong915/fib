package com.fib.tcp.socket;

import org.springframework.util.LinkedCaseInsensitiveMap;

public class Test {

	public static void main(String[] args) {
		LinkedCaseInsensitiveMap<String> map = new LinkedCaseInsensitiveMap<>();
		map.put("aa", "韩信");
		map.put("AA", "李白");
		System.out.println(map);
		System.out.println(map.get("Aa"));
	}

}
