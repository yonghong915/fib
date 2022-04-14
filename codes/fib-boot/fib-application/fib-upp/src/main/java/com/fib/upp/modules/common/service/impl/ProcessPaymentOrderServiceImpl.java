package com.fib.upp.modules.common.service.impl;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.commons.exception.CommonException;
import com.fib.core.util.StatusCode;
import com.fib.core.util.ThreadLocalMessageContext;
import com.fib.upp.modules.common.entity.PaymentOrderStepSetting;
import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;

@Service("processPaymentOrder")
public class ProcessPaymentOrderServiceImpl implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentOrderServiceImpl.class);

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("ProcessPaymentOrderServiceImpl...>{}", context);
		Assert.notNull(context, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		final Map<String, Object> newContext = validateContextByDefault(context);

		// PaymentOrderStepSetting paymentOrderStepSettingId
		ThreadLocalMessageContext.INSTANCE.set("paymentOrderStepSetting", newContext);
		Map<String, Object> rtnMap = MapUtil.newHashMap();

		//
		// buildOrderContext setOppExtMsg

		// createOrder

		// 扩展的增强校验 checkOrderExternalMsg

		// processOrder
		// ----------
		// 通过销售流程配置，决定是否进行订单处理
		boolean processOrder = triggerProcessOrder();
		LOGGER.info("processOrder={}", processOrder);
		rtnMap.put("aaa", "sss");
		return rtnMap;
	}

	/**
	 * 校验是否需要执行订单处理
	 * 
	 * @param context
	 * @return
	 */
	public boolean triggerProcessOrder() {
		return checkSalesStep("processOrder");
	}

	/**
	 * 校验是否需要执行支付处理
	 * 
	 * @param context
	 * @return
	 */
	public boolean triggerProcessPayment() {
		return checkSalesStep("processPayment");
	}

	/**
	 * 校验是否需要执行核准处理
	 * 
	 * @param context
	 * @return
	 */
	public boolean triggerApproveOrder() {
		return checkSalesStep("approveOrder");
	}

	/**
	 * 校验是否需要执行开发票处理
	 * 
	 * @param context
	 * @return
	 */
	public boolean triggerProcessInvoice() {
		return checkSalesStep("processInvoice");
	}

	/**
	 * 校验是否需要执行交付处理
	 * 
	 * @param context
	 * @return
	 */
	public boolean triggerFulfill() {
		return checkSalesStep("fulfill");
	}

	private boolean checkSalesStep(String step) {
		boolean result = true;
		PaymentOrderStepSetting poss = ThreadLocalMessageContext.INSTANCE.get("paymentOrderStepSetting",
				PaymentOrderStepSetting.class);
		if (null != poss) {
			String possId = poss.getPaymentOrderStepSettingId();

			result = !("N".equals(possId) || possId.equals(step));
		}
		return result;
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
		String transactionId = MapUtil.getStr(context, Constant.FieldKey.TRANS_ID.code());
		if (CharSequenceUtil.isNotBlank(transactionId)) {// 如果为 false 抛出给定的异常
			Assert.isTrue(isTransactionIdUnique(transactionId),
					() -> new BusinessException(StatusCode.PARAMS_DUPLICATE, transactionId));
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
		Assert.notBlank(tradePartyId, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		// Party partyId
		return null;
	}

	private boolean isTransactionIdUnique(String transactionId) {
		Assert.notBlank(transactionId, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		// OrderHeader transactionId
		return false;
	}

	public static void main(String[] args) {
		AtomicInteger cnt = new AtomicInteger(0);
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				Map<String, Object> pam = MapUtil.newHashMap();
				pam.put("111", "aaaa" + cnt.getAndIncrement());
				ThreadLocalMessageContext.INSTANCE.set("parmaMap", pam);
				LOGGER.info("{} {}", Thread.currentThread().getName(),
						ThreadLocalMessageContext.INSTANCE.get("parmaMap", Map.class));
			}).start();
		}
	}
}
