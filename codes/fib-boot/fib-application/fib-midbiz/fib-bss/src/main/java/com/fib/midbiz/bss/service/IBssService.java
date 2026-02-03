package com.fib.midbiz.bss.service;

import com.fib.midbiz.common.dto.BssDTO;

public interface IBssService {
	int createBss(BssDTO bssDto);

	BssDTO getBssById(BssDTO bssDto);
}
