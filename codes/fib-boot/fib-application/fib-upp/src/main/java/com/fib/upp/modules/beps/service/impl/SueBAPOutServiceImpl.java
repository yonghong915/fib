package com.fib.upp.modules.beps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.commons.util.CommUtils;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.beps.entity.BatchProcess;
import com.fib.upp.modules.beps.entity.BatchProcessGroup;
import com.fib.upp.modules.beps.service.IBatchProcessService;
import com.fib.upp.modules.beps.service.ISueBAPOutService;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
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
		Integer transNum = MapUtil.getInt(context, Constant.FieldKey.TRANS_NUM.code());
		String transAmtStr = MapUtil.getStr(context, Constant.FieldKey.TRANS_AMT.code());
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

		BigDecimal transAmt = new BigDecimal(transAmtStr);
		Assert.isTrue(!"0".equals(acctStat), () -> new BusinessException(StatusCode.ACCT_STAT_INCOMP));
		Assert.isTrue(new BigDecimal(acctBal).compareTo(transAmt) < 0,
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
		BatchProcessGroup bpg = new BatchProcessGroup();
		bpg.setTransNum(transNum);
		bpg.setTransAmt(transAmt);
		bpg.setBatchType(Constant.BatchType.SUE_BAP_OUT.code());
		bpg.setTransId("50100" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmssSSS")
				+ CommUtils.getRandom("0123456789", 10));
		bpg.setBizType(bizType);
		bpg.setDraweeAcctNo(draweeAcctNo);
		bpg.setDraweeAcctName(draweeAcctName);
		bpg.setPartyId("1101");
		bpg.setTellerId("1219");

		String batchGroupId = "122434444";
		String draweeClearBankCode = "10002334345";
		batchProcessService.insertBatchProcessGroup(bpg);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = MapUtil.get(rtnMap, "dataList", List.class);

		String payeeBankCode = null;
		String payeeClearBankCode = null;
		Opt<BatchProcess> bpOpt = null;
		for (Map<String, Object> dataMap : dataList) {
			payeeBankCode = MapUtil.getStr(dataMap, Constant.FieldKey.PAYEE_BANK_CODE.code());
			payeeClearBankCode = MapUtil.getStr(dataMap, Constant.FieldKey.PAYEE_CLEAR_BANK_CODE.code());
			BigDecimal transSumDetail = new BigDecimal(MapUtil.getStr(dataMap, Constant.FieldKey.TRANS_AMT.code()));
			// 校验小额限额 amtLmt
			// 创建批次：
			// 根据清算行行号+批次头+业务类型+批次类型+批次状态（登记） 查询批次表 如果有累加金额与笔数，如果没有就新建
			BatchProcess bpParam = new BatchProcess();
			bpParam.setSndClearBankCode(draweeClearBankCode);
			bpParam.setRcvClearBankCode(payeeClearBankCode);
			bpParam.setBatchGroupId(batchGroupId);
			bpParam.setBizType(bizType);
			bpParam.setBatchType(Constant.BatchType.SUE_BAP_OUT.code());
			bpParam.setProcessStatus(Constant.BatchStatus.REGISTER.code());

			AtomicReference<BatchProcess> arBp = new AtomicReference<>();
			List<BatchProcess> bpList = new ArrayList<>();
			if (bpList.contains(bpParam)) {

			}

			bpOpt = batchProcessService.getBatchProcess(bpParam);
			if (null != bpOpt && bpOpt.isPresent()) {
				bpParam.setTransNum(bpOpt.get().getTransNum() + 1);
				bpParam.setTransAmt(bpOpt.get().getTransAmt().add(transSumDetail));
				bpParam.setBatchId(bpOpt.get().getBatchId());
				batchProcessService.updateBatchProcess(bpParam);
			} else {
				bpParam.setTransNum(1);
				batchProcessService.insertBatchProcess(bpParam);
			}

			// Map<String, Object> bpdMap = buildBatchProcessDetail(batchId, dataMap);
		}

		return null;
	}

	public static void main(String[] args) {
		List<Map<String, Object>> dataList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			String randDom = CommUtils.getRandom("0123456789", 1);
			Map<String, Object> bpMap = new HashMap<>();
			String[] clearBankCode = { "4564456456", "4564456457" };
			String[] bizType = { "C211", "E101" };
			if (randDom.equals(String.valueOf(i))) {
				bpMap.put("draweeClearBankCode", "313948495");
				bpMap.put("payeeClearBankCode", clearBankCode[0]);
				bpMap.put("batchGroupId", "100001");
				bpMap.put("bizType", bizType[0]);
				bpMap.put("transAmt", "10");
				bpMap.put("batchType", Constant.BatchType.SUE_BAP_OUT.code());
				bpMap.put("brocessStatus", Constant.BatchStatus.REGISTER.code());
			} else {
				bpMap.put("draweeClearBankCode", "313948495");
				bpMap.put("payeeClearBankCode", clearBankCode[1]);
				bpMap.put("batchGroupId", "100001");
				bpMap.put("bizType", bizType[1]);
				bpMap.put("transAmt", "10");
				bpMap.put("batchType", Constant.BatchType.SUE_BAP_OUT.code());
				bpMap.put("brocessStatus", Constant.BatchStatus.REGISTER.code());
			}
			dataList.add(bpMap);
		}

		Map<String, BatchProcess> bpMap = new HashMap<>();
		for (Map<String, Object> dataMap : dataList) {
			String draweeClearBankCode = MapUtil.getStr(dataMap, "draweeClearBankCode");
			String payeeClearBankCode = MapUtil.getStr(dataMap, "payeeClearBankCode");
			String batchGroupId = MapUtil.getStr(dataMap, "batchGroupId");
			String bizType = MapUtil.getStr(dataMap, "bizType");
			String transAmt = MapUtil.getStr(dataMap, "transAmt");
			StringJoiner sj = new StringJoiner("|");
			sj.add(draweeClearBankCode).add(payeeClearBankCode).add(batchGroupId).add(bizType)
					.add(Constant.BatchType.SUE_BAP_OUT.code()).add(Constant.BatchStatus.REGISTER.code());
			String bpKey = sj.toString();
			if (bpMap.containsKey(bpKey)) {
				BatchProcess bp = MapUtil.get(bpMap, bpKey, BatchProcess.class);
				bp.setTransNum(bp.getTransNum() + 1);
				bp.setTransAmt(bp.getTransAmt().add(new BigDecimal(transAmt)));
			} else {
				BatchProcess bpParam = new BatchProcess();
				bpParam.setSndClearBankCode(draweeClearBankCode);
				bpParam.setRcvClearBankCode(payeeClearBankCode);
				bpParam.setBatchGroupId(batchGroupId);
				bpParam.setBizType(bizType);
				bpParam.setBatchType(Constant.BatchType.SUE_BAP_OUT.code());
				bpParam.setProcessStatus(Constant.BatchStatus.REGISTER.code());

				bpParam.setTransAmt(new BigDecimal(transAmt));
				bpParam.setTransNum(1);

				bpMap.put(bpKey, bpParam);
			}
		}
		Iterator<Entry<String, BatchProcess>> iter = bpMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, BatchProcess> s = iter.next();
			String key = s.getKey();
			BatchProcess bps = s.getValue();
			System.out.println("key=" + key + "   bps=" + bps.getTransNum() + " " + bps.getTransAmt());
		}
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
