package com.fib.pay.hvps.api;

import org.springframework.stereotype.Service;

import com.fib.pay.hvps.service.IBssService;
import com.fib.pay.common.dto.BssDTO;

@Service
public class BssServiceApiImpl implements BssServiceApi {

	private IBssService bssService;

	public BssServiceApiImpl(IBssService bssService) {
		this.bssService = bssService;
	}

	@Override
	public int createBss(BssDTO bssDto) {
		return bssService.createBss(bssDto);
	}

	public BssDTO getBssById(BssDTO bssDto) {
		return bssService.getBssById(bssDto);
	}
}
