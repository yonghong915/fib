package com.fib.upp.modules.common.service.impl;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.CommonException;
import com.fib.core.util.ThreadLocalMessageContext;
import com.fib.upp.modules.common.service.ICommonService;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;

@Service("processPaymentOrder")
public class ProcessPaymentOrderServiceImpl implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentOrderServiceImpl.class);

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("ProcessPaymentOrderServiceImpl...>{}", context);
		Map<String, Object> newContext = null;

		newContext = validateContextByDefault(context);

		// PaymentOrderStepSetting paymentOrderStepSettingId
		ThreadLocalMessageContext.INSTANCE.getMessageContext().setProperty("paymentOrderStepSetting", newContext);
		Assert.isNull(newContext, () -> new CommonException("systemCode"));
//		isNull(newContext, () -> ThreadLocalMessageContext.INSTANCE.getMessageContext()
//				.setProperty("paymentOrderStepSetting", newContext));
		return null;
	}

	public static void isNull(Object object, Consumer errorSupplier) {
		if (null != object) {
			errorSupplier.accept(object);
		}
	}

	/**
	 * 包括以下校验内容 1、流水号是否重复的判断，考虑以后将该校验在配置中增加开关 2、所有基础数据的合法性检查，考虑以后将这些检查在配置中增加开关
	 * 3、业务个性化数据的合法性检查，如客户编号等；把柜员的校验也合并到个性化数据校验中；以后增加开关 4、联合必输项的校验 5、产品变型转换
	 * 6、交易机构到商店编号的转换 7、默认值设置
	 */
	private Map<String, Object> validateContextByDefault(Map<String, ? extends Object> context) {
		LOGGER.info("validate order start....");
		Map<String, Object> newContext = MapUtil.newHashMap();
		// 流水号唯一校验
		String transactionId = MapUtil.getStr(context, "transactionId");
		if (CharSequenceUtil.isNotBlank(transactionId)) {// 如果为 false 抛出给定的异常
			Assert.isTrue(isTransactionIdUnique(transactionId), () -> new CommonException("systemCode"));
		}

		// 交易机构
		String tradePartyId = MapUtil.getStr(context, "tradePartyId");
		if (CharSequenceUtil.isNotBlank(tradePartyId)) {// 如果为 false 抛出给定的异常
			Assert.notNull(getParty(tradePartyId), () -> new CommonException("systemCode"));
		}

		// 校验柜员
		// checkUserLogin
		LOGGER.info("validate order end....");
		return newContext;
	}

	private Object getParty(String tradePartyId) {
		// Party partyId
		return null;
	}

	private boolean isTransactionIdUnique(String transactionId) {
		// OrderHeader transactionId
		return false;
	}

	public static void main(String[] args) {
		Assert.isFalse(true, () -> new CommonException("systemCode"));
	}

}
