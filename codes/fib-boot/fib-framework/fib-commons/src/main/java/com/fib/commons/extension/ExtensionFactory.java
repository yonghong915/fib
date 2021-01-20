package com.fib.commons.extension;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
@SPI
public interface ExtensionFactory {
	<T> T getExtension(Class<T> type, String name);
}
