package com.fib.commons.extension;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-20
 */
public @interface Wrapper {

	String[] matches() default {};

	String[] mismatches() default {};
}