package com.fib.upp.modules.cnaps.beps.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.core.base.dto.BaseDTO;
import com.fib.upp.modules.cnaps.beps.entity.MessagePackRule;

import cn.hutool.core.lang.Opt;

/**
 * 报文组包规则服务
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-22
 */
public interface IMessagePackRuleService extends IService<MessagePackRule> {
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
	Opt<MessagePackRule> getMessagePackRule(BaseDTO dto);
}
