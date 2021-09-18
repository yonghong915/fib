package com.fib.upp.service.rulecheck;

/**
 * 规则校验
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-18
 * @param <T>
 */
public interface IRuleCheckService<T> {
	boolean check(T dto);
}
