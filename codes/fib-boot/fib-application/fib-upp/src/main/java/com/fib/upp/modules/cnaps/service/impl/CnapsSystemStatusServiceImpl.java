package com.fib.upp.modules.cnaps.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.commons.exception.BusinessException;
import com.fib.commons.exception.CommonException;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;
import com.fib.upp.modules.cnaps.mapper.CnapsSystemStatusMapper;
import com.fib.upp.modules.cnaps.service.ICnapsSystemStatusService;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;

@Service("cnapsSystemStatusService")
public class CnapsSystemStatusServiceImpl extends ServiceImpl<CnapsSystemStatusMapper, CnapsSystemStatus>
		implements ICnapsSystemStatusService {
	private CnapsSystemStatusMapper cnapsSystemStatusMapper;

	public CnapsSystemStatusServiceImpl(CnapsSystemStatusMapper cnapsSystemStatusMapper) {
		this.cnapsSystemStatusMapper = cnapsSystemStatusMapper;
	}

	@Override
	// @EcaAnnotation(service =
	// "inValidate:messagePackRuleService:getMessagePackRule")
	public Opt<CnapsSystemStatus> getCnapsSystemStatus(CnapsSystemStatus css) {
		Assert.notNull(css, () -> new CommonException("css"));
		Assert.notBlank(css.getSysCode(), () -> new CommonException("systemCode"));
		Assert.notBlank(css.getClearBankCode(), () -> new CommonException("clearBankNo"));
		String systemCode = css.getSysCode();
		String clearBankNo = css.getClearBankCode();
		try {
			return Opt.ofNullable(cnapsSystemStatusMapper.getCnapsSystemStatus(systemCode, clearBankNo));
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION, e);
		}
	}
}