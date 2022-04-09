package com.fib.upp.modules.common.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.SpringContextUtils;
import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 往账贷记服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-08 15:11:36
 */
@Service("pETOCOCAndPFfill")
public class PETOCOCAndPFfillServiceImpl implements ICommonService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PETOCOCAndPFfillServiceImpl.class);

	@Override
	public Map<String, Object> execute(Map<String, ? extends Object> context) {
		LOGGER.info("PETOCOCAndPFfillServiceImpl--->{}", context);
		Map<String, Object> requestMap = null;
		requestMap = buildingParam(context);
		Map<String, Object> rspMap = runSync(Constant.ServiceName.PROCESS_PAYMENT_ORDER.code(), requestMap);
		String returnType = MapUtil.getStr(rspMap, "returnType");
		if (!"S".equals(returnType)) {
			throw new BusinessException("aa", "返回错误");
		}
		return rspMap;
	}

	private Map<String, Object> buildingParam(Map<String, ? extends Object> context) {
		Map<String, Object> requestMap = MapUtil.<String, Object>builder().build();
		String systemCode = MapUtil.getStr(context, "systemCode");
		//
		String transAmt = MapUtil.getStr(context, "transAmt");
		systemCode = obtainSystemCode(systemCode, transAmt);

		requestMap.put("systemCode", systemCode);
		return requestMap;
	}

	private String obtainSystemCode(String systemCode, String transAmt) {
		if (CharSequenceUtil.isNotEmpty(systemCode)) {
			return systemCode;
		}
		String isPosthaste = "";
		// ProductCategoryMember productId
		List<String> list = CollUtil.newArrayList();
		if (CollUtil.isEmpty(list)) {
			throw new BusinessException("aa", "系统中不存在当前业务");
		}
		int len = list.size();
		if (len == 1) {
			String productCategoryId = list.get(0);
			if (productCategoryId.contains("BEPS")) {
				systemCode = "BEPS";
				BigDecimal lmtAmt = getLmtAmt(systemCode);
				BigDecimal sum = new BigDecimal(transAmt);
				if (sum.compareTo(lmtAmt) > 0) {
					throw new BusinessException("bbb", "当前交易金额已超过小额限额控制，不能发起小额业务");
				}
			} else if (productCategoryId.contains("HVPS")) {
				systemCode = "HVPS";
			} else {
				throw new BusinessException("ccc", "二代支付系统只支持大额（HVPS）与小额（BEPS）系统下的业务种类，不支持当前业务种类");
			}
		} else {
			if (!"3".equals(isPosthaste)) {
				systemCode = "HVPS";
			} else {
				BigDecimal lmtAmt = getLmtAmt(systemCode);
				BigDecimal sum = new BigDecimal(transAmt);
				systemCode = (sum.compareTo(lmtAmt) > 0) ? "HVPS" : "BEPS";
			}
		}
		return systemCode;
	}

	private static final String DEFAULT_LMT_AMT = "1000000";

	/**
	 * 获取限额
	 * 
	 * @param systemCode
	 * @return
	 */
	private BigDecimal getLmtAmt(String systemCode) {
		ICommonService commSrv = SpringContextUtils.getBean("lmtAmtService");
		Map<String, Object> rspMap = commSrv.execute(null);
		List<String> list = MapUtil.get(rspMap, "lmtAmtLst", List.class);
		String lmtAmt = DEFAULT_LMT_AMT;
		if (CollUtil.isNotEmpty(list)) {
			lmtAmt = list.get(0);
		}
		return new BigDecimal(lmtAmt);
	}

	Map<String, Object> runSync(String serviceName, Map<String, ? extends Object> context) {
		ICommonService commSrv = SpringContextUtils.getBean(serviceName);
		return commSrv.execute(context);
	}

	public void runAsync(String serviceName, Map<String, ? extends Object> context) {
		Executor executor = SpringContextUtils.getBean("customAsyncExcecutor");
		executor.execute(() -> {
			ICommonService commSrv = SpringContextUtils.getBean(serviceName);
			commSrv.execute(context);
		});
	}
}