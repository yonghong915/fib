package com.fib.commons.compiler;

import com.fib.commons.extension.SPI;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
@SPI("javassist")
public interface Compiler {

	/**
	 * Compile java source code.
	 *
	 * @param code        Java source code
	 * @param classLoader classloader
	 * @return Compiled class
	 */
	Class<?> compile(String code, ClassLoader classLoader);

}
