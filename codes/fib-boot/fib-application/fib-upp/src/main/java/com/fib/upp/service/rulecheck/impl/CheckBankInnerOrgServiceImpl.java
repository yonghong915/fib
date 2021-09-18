package com.fib.upp.service.rulecheck.impl;

import org.springframework.stereotype.Service;

import com.fib.upp.service.rulecheck.AbstractRuleCheckService;
import com.fib.upp.service.rulecheck.dto.BankOrgDTO;

/**
 * 行内机构校验
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-18
 */
@Service("checkBankInnerOrgService")
public class CheckBankInnerOrgServiceImpl extends AbstractRuleCheckService<BankOrgDTO> {

	@Override
	public boolean check(BankOrgDTO dto) {
		logger.info("input params:{}", dto);
		return false;
	}
}