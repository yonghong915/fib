package com.fib.upp.modules.beps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.beps.service.IBatchProcessService;
import com.fib.upp.modules.beps.service.ISueBAPOutService;
import com.fib.upp.util.Constant;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 定期贷记
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-15 08:46:53
 */
@Service("sueBAPOutService")
public class SueBAPOutServiceImpl implements ISueBAPOutService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SueBAPOutServiceImpl.class);

	@Autowired
	private IBatchProcessService batchProcessService;

	@Override
	public Map<String, Object> register(Map<String, ? extends Object> context) {
		LOGGER.info("SueBAPOutServiceImpl-->register,params is [{}]", context);
		String bizType = MapUtil.getStr(context, Constant.FieldKey.BIZ_TYPE.code());
		String transNum = MapUtil.getStr(context, Constant.FieldKey.TRANS_NUM.code());
		String transAmt = MapUtil.getStr(context, Constant.FieldKey.TRANS_AMT.code());
		String chanlSerialNo = MapUtil.getStr(context, Constant.FieldKey.CHANL_SERIAL_NO.code());
		String fileName = MapUtil.getStr(context, Constant.FieldKey.FILE_NAME.code());

		String draweeAcctNo = MapUtil.getStr(context, Constant.FieldKey.DRAWEE_ACCT_NO.code());
		Assert.notBlank(draweeAcctNo,
				() -> new BusinessException(StatusCode.PARAMS_CHECK_NULL, Constant.FieldKey.DRAWEE_ACCT_NO.name()));
		String draweeAcctName = MapUtil.getStr(context, Constant.FieldKey.DRAWEE_ACCT_NAME.code());
		// qryAcctInfo 查询账户信息
		// 获取账户状态 acctStat,账户余额 acctBal
		String acctStat = "0";
		String acctBal = "100000000000000000.00";

		Assert.isTrue(!"0".equals(acctStat), () -> new BusinessException(StatusCode.ACCT_STAT_INCOMP));
		Assert.isTrue(new BigDecimal(acctBal).compareTo(new BigDecimal(transAmt)) < 0,
				() -> new BusinessException(StatusCode.ACCT_BAL_INSUFFICIENT));

		// 下载远程文件到本地 FTP
		Map<String, Object> checkMap = MapUtil.newHashMap();
		checkMap.put(Constant.FieldKey.FILE_NAME.code(), fileName);
		checkMap.put(Constant.FieldKey.TRANS_AMT.code(), transAmt);
		checkMap.put(Constant.FieldKey.TRANS_NUM.code(), transNum);
		checkMap.put(Constant.FieldKey.BATCH_TYPE.code(), Constant.BatchType.SUE_BAP_OUT.code());
		checkMap.put(Constant.FieldKey.BIZ_TYPE.code(), bizType);
		Map<String, Object> rtnMap = checkFile(checkMap);

		// 创建批次头信息 BATCH_PROCESS_GROUP

		return null;
	}

	private Map<String, Object> checkFile(Map<String, Object> checkMap) {
		LOGGER.info("checkFile--->params is [{}]", checkMap);

		String transAmtStr = MapUtil.getStr(checkMap, Constant.FieldKey.TRANS_AMT.code());
		Assert.isTrue(NumberUtil.isNumber(transAmtStr), () -> new BusinessException(StatusCode.RTN_NULL, "交易金额不是数字"));
		BigDecimal transAmt = NumberUtil.round(transAmtStr, Constant.ConstantUtil.AMT_SCALE);
		String channel = MapUtil.getStr(checkMap, Constant.FieldKey.CHANNEL.code());
		channel = StrUtil.isEmptyIfStr(transAmt) ? Constant.ConstantUtil.DEF_CHANNEL : channel;
		// FTP下载文件到本地目录
		String localFileName = "D:/datafile/10223232222.txt";
		// 解密文件内容 DESUtil.decrypt
		// 判断文件编码是否UTF-8
		// 校验文件内容
		// 校验总金额、总记录数与传入的参数是否相等
		Map<String, Object> rtnMap = MapUtil.newHashMap();
		rtnMap.put("dataList", new ArrayList<>());
		rtnMap.put("dealAmtSum", 1000.00);
		rtnMap.put("dealNumSum", 100);
		rtnMap.put("localFileName", localFileName);
		return rtnMap;
	}

	@Override
	public Map<String, Object> review(Map<String, ? extends Object> context) {
		return null;
	}

}
