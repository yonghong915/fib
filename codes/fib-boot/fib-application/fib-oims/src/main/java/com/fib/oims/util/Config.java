package com.fib.oims.util;


public class Config {

	public static int getHttpConnectTimeout() {
		return 3000;
	}

	public static int getHttpSocketTimeout() {
		return 3000;
	}

	public static int getHttpMaxPoolSize() {
		return 50;
	}

	public static long getHttpIdelTimeout() {
		return 3000;
	}

	public static long getHttpMonitorInterval() {
		return 3000;
	}
	
}
