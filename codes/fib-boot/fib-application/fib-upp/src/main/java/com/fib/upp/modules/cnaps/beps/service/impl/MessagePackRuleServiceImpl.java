package com.fib.upp.modules.cnaps.beps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.commons.exception.BusinessException;
import com.fib.core.base.dto.BaseDTO;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.cnaps.beps.entity.MessagePackRule;
import com.fib.upp.modules.cnaps.beps.mapper.MessagePackRuleMapper;
import com.fib.upp.modules.cnaps.beps.service.IMessagePackRuleService;

import cn.hutool.core.lang.Opt;

@Service("messagePackRuleService")
public class MessagePackRuleServiceImpl extends ServiceImpl<MessagePackRuleMapper, MessagePackRule>
		implements IMessagePackRuleService {

	@Autowired
	private MessagePackRuleMapper messagePackRuleMapper;

	@Override
	public List<MessagePackRule> queryMessagePackRuleList() {
		try {
			return messagePackRuleMapper.selectList(null);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION,e);
		}
	}

	@Override
	public Opt<MessagePackRule> getMessagePackRule(BaseDTO dto) {
		MessagePackRule mpr = (MessagePackRule) dto;
		String messageTypeCode = mpr.getMessageTypeCode();
		try {
			return Opt.ofNullable(messagePackRuleMapper.getMessagePackRule(messageTypeCode));
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION,e);
		}
	}
}