package com.fib.upp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.entity.cnaps.CnapsSystemStatus;
import com.fib.upp.mapper.CnapsSystemStatusMapper;
import com.fib.upp.service.ICnapsSystemStatusService;

import cn.hutool.core.lang.Opt;

@Service("cnapsSystemStatusService")
public class CnapsSystemStatusServiceImpl extends ServiceImpl<CnapsSystemStatusMapper, CnapsSystemStatus>
		implements ICnapsSystemStatusService {
	private CnapsSystemStatusMapper cnapsSystemStatusMapper;

	public CnapsSystemStatusServiceImpl(CnapsSystemStatusMapper cnapsSystemStatusMapper) {
		this.cnapsSystemStatusMapper = cnapsSystemStatusMapper;
	}

	@Override
	public Opt<CnapsSystemStatus> getCnapsSystemStatus(String systemCode, String clearBankNo) {
		try {
			return Opt.ofNullable(cnapsSystemStatusMapper.getCnapsSystemStatus(systemCode, clearBankNo));
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}
}
