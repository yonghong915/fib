package com.fib.upp.service.rulecheck.impl;

import org.springframework.stereotype.Service;

import com.fib.upp.service.rulecheck.AbstractRuleCheckService;
import com.fib.upp.service.rulecheck.dto.BankOrgDTO;

/**
 * 人行系统状态校验
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-18
 */
@Service("checkCnapsSystemStatusService")
public class CheckCnapsSystemStatusServiceImpl extends AbstractRuleCheckService<BankOrgDTO> {

	@Override
	public boolean check(BankOrgDTO dto) {
		logger.info("input params:{}", dto);
		return true;
	}
}