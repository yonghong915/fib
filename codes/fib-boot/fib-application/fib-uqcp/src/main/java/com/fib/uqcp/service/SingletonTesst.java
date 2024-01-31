package com.fib.uqcp.service;

public class SingletonTesst {

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			SingletonExample instance = SingletonExample.getInstance();
			System.out.println(instance);
		}
	}

}
