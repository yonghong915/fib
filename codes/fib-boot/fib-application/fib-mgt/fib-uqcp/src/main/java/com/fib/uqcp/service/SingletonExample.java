package com.fib.uqcp.service;

/**
 * 枚举 线程安全
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-18 14:57:18
 */
public class SingletonExample {
	/**
	 * 构造函数私有化，避免外部创建实例
	 */
	private SingletonExample() {
	}

	public static SingletonExample getInstance() {
		return Singleton.INSTANCE.getInstance();
	}

	private enum Singleton {
		INSTANCE;

		private SingletonExample instance;

		// JVM 保证这个方法绝对只调用一次
		Singleton() {
			instance = new SingletonExample();
		}

		public SingletonExample getInstance() {
			return instance;
		}
	}
}
