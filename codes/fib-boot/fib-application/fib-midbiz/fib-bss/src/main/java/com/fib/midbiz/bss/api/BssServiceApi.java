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
	BssDTO getBssById(Long bssId);
}
