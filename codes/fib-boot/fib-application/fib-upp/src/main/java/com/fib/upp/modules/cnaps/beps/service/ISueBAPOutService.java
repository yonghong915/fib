package com.fib.upp.modules.cnaps.beps.service;

import java.util.Map;

public interface ISueBAPOutService {

	/**
	 * 登记
	 * 
	 * @param context
	 * @return
	 */
	public Map<String, Object> register(Map<String, ? extends Object> context);

	/**
	 * 定期借贷记复核
	 * 
	 * @param context
	 * @return
	 */
	public Map<String, Object> review(Map<String, ? extends Object> context);
}
