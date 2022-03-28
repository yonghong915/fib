package com.fib.upp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.entity.cnaps.CnapsSystemStatus;
import com.fib.upp.util.EnumConstants;

@DS(EnumConstants.DATASOURCE_UPP)
public interface CnapsSystemStatusMapper extends BaseMapper<CnapsSystemStatus> {
	CnapsSystemStatus getCnapsSystemStatus(String systemCode, String clearBankNo);
}
