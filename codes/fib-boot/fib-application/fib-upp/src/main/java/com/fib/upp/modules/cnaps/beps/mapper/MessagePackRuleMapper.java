package com.fib.upp.modules.cnaps.beps.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.modules.cnaps.beps.entity.MessagePackRule;
import com.fib.upp.util.UppEnumConstants;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-24
 */

@DS(UppEnumConstants.DATASOURCE_UPP)
public interface MessagePackRuleMapper extends BaseMapper<MessagePackRule> {

	MessagePackRule getMessagePackRule(String messageTypeCode);
}
