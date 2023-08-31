package com.fib.test;

public class TestEnv {

	public static void main(String[] args) {
		System.out.println(System.getenv("GOPATH"));
		System.out.println(System.getProperty("GOPATH"));

	}

}
