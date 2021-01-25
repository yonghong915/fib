package com.fib.upp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.upp.mapper.BepsMessagePackRuleMapper;
import com.fib.upp.pay.beps.pack.BepsMessagePackRule;
import com.fib.upp.service.IBepsPackService;

@Service("bepsPackService")
public class BepsPackServiceImpl implements IBepsPackService {
	@Autowired
	private BepsMessagePackRuleMapper bepsMessagePackRuleMapper;

	@Override
	public List<BepsMessagePackRule> queryBepsPackRuleList() {
		QueryWrapper<BepsMessagePackRule> wrapper  = new QueryWrapper<>();
		return bepsMessagePackRuleMapper.selectList(wrapper);
	}
}
