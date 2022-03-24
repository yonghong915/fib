package com.fib.upp.util;

/**
 * 二进制状态工具类
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-24 15:13:24
 */
public class BitStateUtil {
	private BitStateUtil() {
		// nothing to do
	}

	/**
	 * 判断是否含有状态
	 * 
	 * @param states 所有状态值
	 * @param value  需要判断状态值
	 * @return 是否存在
	 */
	public static boolean has(long states, long value) {
		return (states & value) != 0;
	}

	/**
	 * 增加状态
	 * 
	 * @param states 已有状态值
	 * @param value  需要添加状态值
	 * @return 新的状态值
	 */
	public static long add(long states, long value) {
		if (has(states, value)) {
			return states;
		}
		return (states | value);
	}

	/**
	 * 删除一个状态
	 * 
	 * @param states 已有状态值
	 * @param value  需要删除状态值
	 * @return 新的状态值
	 */
	public static long remove(long states, long value) {
		if (has(states, value)) {
			return states ^ value;
		}
		return states;
	}
}