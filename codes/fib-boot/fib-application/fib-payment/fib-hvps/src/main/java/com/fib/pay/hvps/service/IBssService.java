package com.fib.pay.hvps.service;

import com.fib.pay.common.dto.BssDTO;

public interface IBssService {
	int createBss(BssDTO bssDto);

	BssDTO getBssById(BssDTO bssDto);
}
