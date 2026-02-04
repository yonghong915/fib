package com.fib.uqcp.batch;

import java.util.List;

import com.fib.uqcp.batch.taskstep.BatchGenericException;

/**
 * 扫描器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-08 21:12:34
 * @param <J>
 */
public interface IScanner<T> {
	public List<T> scan() throws BatchGenericException;
}
