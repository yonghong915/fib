package com.fib.upp.modules.cnaps.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.commons.exception.BusinessException;
import com.fib.commons.util.CommUtils;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;
import com.fib.upp.modules.cnaps.service.ICnapsSystemStatusService;
import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 系统状态变更通知报文<ccms.801.001.0X>
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-25 11:25:35
 */
@Service("updateCnapsSystemStatus")
public class CnapsSystemStatusUpdateService implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CnapsSystemStatusUpdateService.class);

	@Autowired
	private ICnapsSystemStatusService cnapsSystemStatusService;

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("updateCnapsSystemStatus-->params is [{}]", context);
		@SuppressWarnings("unchecked")
		Map<String, Object> extMap = MapUtil.get(context, "extMap", Map.class);
		Assert.notNull(extMap, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		String clearBankNo = MapUtil.getStr(extMap, Constant.FieldKey.CLEAR_BANK_CODE.code());
		String productId = MapUtil.getStr(extMap, "productId");
		String sysCode = MapUtil.getStr(extMap, Constant.FieldKey.SYS_CODE.code());
		CnapsSystemStatus css = new CnapsSystemStatus();
		BeanUtil.fillBeanWithMap(extMap, css, false);
		css.setPkId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));
		if ("X021cm801".equals(productId)) {
			// 异步通知其他平台二代支付通道状态
			QueryWrapper<CnapsSystemStatus> updateWrapper = new QueryWrapper<>();
			updateWrapper.eq(CharSequenceUtil.toUnderlineCase(Constant.FieldKey.SYS_CODE.code()), sysCode);
			updateWrapper.eq(CharSequenceUtil.toUnderlineCase(Constant.FieldKey.CLEAR_BANK_CODE.code()), clearBankNo);
			cnapsSystemStatusService.saveOrUpdate(css, updateWrapper);
		} else if ("X025cm807".equals(productId)) {
			// 强制离线将该通道的状态设为退出状态 loginOperType=OT01
		}
		return MapUtil.of("returnType", "S");
	}
}