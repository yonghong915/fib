package com.fib.upp.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fib.upp.service.ICommonService;
import com.fib.upp.util.EnumConstants;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 通用服务
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-23
 */
@Service("commonService")
public class CommonServiceImpl implements ICommonService {
	final int DEFAULT_STR_LEN = 8;

	@Override
	public String getTransactionId(String systemCode) {
		String systemDate = getCnapsSystemDate(EnumConstants.CLEAR_BANK_CODE, systemCode);
		String seqId = getNextSeqId(EnumConstants.TX_ID_SEQ_NAME);
		seqId = StrUtil.fillBefore(seqId, '0', DEFAULT_STR_LEN);
		return systemDate + seqId;
	}

	@Override
	public String getMessageId(String systemCode) {
		String systemDate = getCnapsSystemDate(EnumConstants.CLEAR_BANK_CODE, systemCode);
		String seqId = getNextSeqId(EnumConstants.MESSAGE_ID_SEQ_NAME);
		seqId = StrUtil.fillBefore(seqId, '0', DEFAULT_STR_LEN);
		return systemDate + seqId;
	}

	@Transactional
	@Override
	public String getCnapsSystemDate(String clearBankCode, String systemCode) {
		// CURRENT_SYSTEM_DATE from CNAPS_SYSTEM_STATUS where CLEAR_BANK_NO = ? and
		// SYSTEM_CODE = ?
		return DateUtil.format(new Date(), "yyyyMMdd");
	}

	@Override
	public String getNextSeqId(String seqName) {
		// 取数据库序列
		return RandomUtil.randomNumbers(7);
	}
}