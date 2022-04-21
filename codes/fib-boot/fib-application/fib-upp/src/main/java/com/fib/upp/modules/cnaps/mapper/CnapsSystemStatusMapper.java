package com.fib.upp.modules.cnaps.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;
import com.fib.upp.util.UppEnumConstants;

@DS(UppEnumConstants.DATASOURCE_UPP)
public interface CnapsSystemStatusMapper extends BaseMapper<CnapsSystemStatus> {
	CnapsSystemStatus getCnapsSystemStatus(String systemCode, String clearBankNo);
}
