package com.fib.pay.hvps.api;

import org.springframework.stereotype.Component;

import com.fib.pay.common.dto.BssDTO;

@Component
public interface BssServiceApi {
	/**
	 * 
	 * @param bssId
	 * @return
	 */
	int createBss(BssDTO bssDto);

	BssDTO getBssById(BssDTO bssDto);
}
