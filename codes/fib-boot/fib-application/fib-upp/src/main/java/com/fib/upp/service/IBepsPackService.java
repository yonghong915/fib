package com.fib.upp.service;

import java.util.List;

import com.fib.upp.pay.beps.pack.BepsMessagePackRule;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-22
 */
public interface IBepsPackService {
	public List<BepsMessagePackRule> queryBepsPackRuleList();
}
