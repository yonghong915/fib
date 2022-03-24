package com.fib.upp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.upp.entity.MessagePackRule;
import com.fib.upp.mapper.MessagePackRuleMapper;
import com.fib.upp.service.IMessagePackRuleService;

import cn.hutool.core.lang.Opt;

@Service("messagePackRuleService")
public class MessagePackRuleServiceImpl implements IMessagePackRuleService {

	@Autowired
	private MessagePackRuleMapper messagePackRuleMapper;

	@Override
	public List<MessagePackRule> queryMessagePackRuleList() {
		return messagePackRuleMapper.selectList(null);
	}

	@Override
	public Opt<MessagePackRule> getMessagePackRule(String messageTypeCode) {
		return Opt.ofNullable(messagePackRuleMapper.getMessagePackRule(messageTypeCode));
	}
}