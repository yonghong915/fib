package com.fib.midbiz.efs.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fib.midbiz.bss.api.BssServiceApi;
import com.fib.midbiz.common.dto.BssDTO;
import com.fib.midbiz.common.dto.EfsDTO;
import com.fib.midbiz.efs.api.EfsServiceApi;
import com.fib.midbiz.efs.entity.EfsEntity;
import com.fib.midbiz.efs.mapper.EfsMapper;

import cn.hutool.core.util.IdUtil;

@Service("efsService")
public class EfsServiceImpl implements EfsServiceApi {
	private BssServiceApi bssServiceApi;

	private EfsMapper efsMapper;

	public EfsServiceImpl(BssServiceApi bssServiceApi, EfsMapper efsMapper) {
		this.bssServiceApi = bssServiceApi;
		this.efsMapper = efsMapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public EfsDTO createEfs() {
		Long bssId = IdUtil.getSnowflakeNextId();
		BssDTO bssDTO = new BssDTO();
		bssDTO.setId(bssId);
		int rows = bssServiceApi.createBss(bssDTO);
		if (rows == 0) {
			throw new RuntimeException("用户不存在");
		}

		BssDTO qryDto = bssServiceApi.getBssById(bssDTO);
		if (qryDto == null) {
			throw new RuntimeException("用户不存在");
		}
		EfsDTO efsDTO = new EfsDTO();
		EfsEntity efsEntity = new EfsEntity();
		efsEntity.setId(2018260969310363648l);
		efsMapper.createEfs(efsEntity);

		return efsDTO;
	}
}