package com.fib.midbiz.bss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.midbiz.bss.api.BssServiceApi;
import com.fib.midbiz.bss.entity.BssEntity;
import com.fib.midbiz.bss.mapper.BssMapper;
import com.fib.midbiz.bss.service.IBssService;
import com.fib.midbiz.common.dto.BssDTO;

@Service("bssService")
public class BssServiceImpl implements IBssService, BssServiceApi {
	@Autowired
	private BssMapper bssMapper;

	@Override
	public BssDTO getBssById(Long bssId) {
		BssDTO bssDTO = new BssDTO();
		BssEntity bssEntity = new BssEntity();
		bssEntity.setId(bssId);
		bssMapper.createBss(bssEntity);
		return bssDTO;
	}
}