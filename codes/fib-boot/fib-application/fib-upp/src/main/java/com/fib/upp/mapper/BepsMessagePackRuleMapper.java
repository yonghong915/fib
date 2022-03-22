package com.fib.upp.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.upp.entity.MessagePackRule;
import com.fib.upp.util.EnumConstants;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-24
 */

@DS(EnumConstants.DATASOURCE_UPP)
public interface BepsMessagePackRuleMapper extends BaseMapper<MessagePackRule> {

	MessagePackRule getMessagePackRule(String messageTypeCode);
}
