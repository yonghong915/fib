package com.fib.upp.service;

import java.util.List;

import com.fib.upp.entity.MessagePackRule;

import cn.hutool.core.lang.Opt;

/**
 * 报文组包规则服务
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-22
 */
public interface IMessagePackRuleService {
	/**
	 * 获取报文组包规则
	 * 
	 * @return
	 */
	public List<MessagePackRule> queryMessagePackRuleList();

	/**
	 * 通过报文类型获取报文组包规则
	 * 
	 * @param messageTypeCode
	 * @return
	 */
	Opt<MessagePackRule> getMessagePackRule(String messageTypeCode);
}
