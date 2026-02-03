package com.fib.midbiz.bss.api;

import org.springframework.stereotype.Component;

import com.fib.midbiz.common.dto.BssDTO;

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
