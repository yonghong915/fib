package com.fib.midbiz.bss.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.midbiz.bss.entity.BssEntity;
import com.fib.midbiz.bss.mapper.BssMapper;
import com.fib.midbiz.bss.service.IBssService;
import com.fib.midbiz.bss.struct.BssStructMapper;
import com.fib.midbiz.common.dto.BssDTO;

@Service("bssService")
public class BssServiceImpl implements IBssService {
	@Autowired
	private BssMapper bssMapper;

	@Autowired
	private BssStructMapper bssStructMapper;

	@Override
	public int createBss(BssDTO bssDto) {
		BssEntity bssEntity = bssStructMapper.dtoToEntity(bssDto);
		int rows = bssMapper.createBss(bssEntity);
		return rows;
	}

	@Override
	public BssDTO getBssById(BssDTO bssDto) {
		Optional<BssEntity> bssOptional = bssMapper.getBssById(bssDto.getId());
		BssEntity bssEntity = bssOptional.orElseThrow(() -> new BusinessException());
		return bssStructMapper.entityToDto(bssEntity);
	}

	private class BusinessException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}
}