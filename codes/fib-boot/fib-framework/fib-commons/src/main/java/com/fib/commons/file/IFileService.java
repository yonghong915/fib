package com.fib.commons.file;

/**
 * 文件执行服务
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-29
 * @param <T>
 */
public interface IFileService<T> {

	/**
	 * 执行
	 * 
	 * @param fileName
	 * @return
	 */
	T execute(String fileName);
}
