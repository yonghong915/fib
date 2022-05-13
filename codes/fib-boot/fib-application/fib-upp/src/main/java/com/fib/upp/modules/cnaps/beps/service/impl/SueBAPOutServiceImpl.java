package com.fib.upp.modules.cnaps.beps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcess;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessDetail;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessGroup;
import com.fib.upp.modules.cnaps.beps.service.IBatchProcessService;
import com.fib.upp.modules.cnaps.beps.service.ISueBAPOutService;
import com.fib.upp.modules.common.service.ServiceDispatcher;
import com.fib.upp.util.Constant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
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
	private static final String DELIMITER = "!^";

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
		bpg.setBizType(bizType);
		bpg.setDraweeAcctNo(draweeAcctNo);
		bpg.setDraweeAcctName(draweeAcctName);
		bpg.setTransId(chanlSerialNo);
		bpg.setPartyId("1101");
		bpg.setTellerId("1219");

		String batchGroupId = "122434444";
		String draweeClearBankCode = "10002334345";
		batchProcessService.insertBatchProcessGroup(bpg);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = MapUtil.get(rtnMap, "dataList", List.class);

		String payeeClearBankCode = null;
		List<BatchProcessDetail> bpdList = CollUtil.newArrayList();
		BatchProcessDetail bpd = null;
		Map<String, BatchProcess> bpMap = MapUtil.newHashMap();
		String batchId = null;
		for (Map<String, Object> dataMap : dataList) {
			payeeClearBankCode = MapUtil.getStr(dataMap, Constant.FieldKey.PAYEE_CLEAR_BANK_CODE.code());
			BigDecimal transSumDetail = new BigDecimal(MapUtil.getStr(dataMap, Constant.FieldKey.TRANS_AMT.code()));
			// 校验小额限额 amtLmt
			// 创建批次：
			// 根据清算行行号+批次头+业务类型+批次类型+批次状态（登记） 查询批次表 如果有累加金额与笔数，如果没有就新建

			StringJoiner joiner = new StringJoiner(DELIMITER);
			joiner.add(draweeClearBankCode).add(payeeClearBankCode).add(batchGroupId).add(bizType)
					.add(Constant.BatchType.SUE_BAP_OUT.code()).add(Constant.BatchStatus.REGISTER.code());
			String bpKey = joiner.toString();
			if (bpMap.containsKey(bpKey)) {
				BatchProcess bp = MapUtil.get(bpMap, bpKey, BatchProcess.class);
				batchId = bp.getBatchId();
				bp.setTransNum(bp.getTransNum() + 1);
				bp.setTransAmt(bp.getTransAmt().add(transSumDetail));
			} else {
				BatchProcess bpParam = new BatchProcess();
				batchId = IdUtil.randomUUID();
				bpParam.setBatchId(batchId);
				bpParam.setSndClearBankCode(draweeClearBankCode);
				bpParam.setRcvClearBankCode(payeeClearBankCode);
				bpParam.setBatchGroupId(batchGroupId);
				bpParam.setBizType(bizType);
				bpParam.setBatchType(Constant.BatchType.SUE_BAP_OUT.code());
				bpParam.setProcessStatus(Constant.BatchStatus.REGISTER.code());

				bpParam.setTransAmt(transSumDetail);
				bpParam.setTransNum(1);

				bpMap.put(bpKey, bpParam);
			}

			bpd = buildBatchProcessDetail(batchId, dataMap);
			bpdList.add(bpd);

			if (bpdList.size() == 100) {
				batchProcessService.insertBatch4BPD(bpdList);
				bpdList = CollUtil.newArrayList();
			}
		}

		if (!bpdList.isEmpty()) {
			batchProcessService.insertBatch4BPD(bpdList);
		}

		// 入库
		Iterator<Entry<String, BatchProcess>> iter = bpMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, BatchProcess> entry = iter.next();
			batchProcessService.insertBatchProcess(entry.getValue());
		}
		return rtnMap;
	}

	private BatchProcessDetail buildBatchProcessDetail(String batchId, Map<String, Object> dataMap) {
		BatchProcessDetail bpd = new BatchProcessDetail();
		bpd.setBatchId(batchId);
		bpd.setBizType(MapUtil.getStr(dataMap, Constant.FieldKey.BIZ_TYPE.code()));
		bpd.setDraweeAcctNo(MapUtil.getStr(dataMap, Constant.FieldKey.DRAWEE_ACCT_NO.code()));
		return bpd;
	}

	private Map<String, Object> checkFile(Map<String, Object> checkMap) {
		LOGGER.info("checkFile--->params is [{}]", checkMap);

		String transAmtStr = MapUtil.getStr(checkMap, Constant.FieldKey.TRANS_AMT.code());
		Assert.isTrue(NumberUtil.isNumber(transAmtStr), () -> new BusinessException(StatusCode.RTN_NULL, "交易金额不是数字"));
		BigDecimal transAmt = NumberUtil.round(transAmtStr, Constant.ConstantUtil.AMT_SCALE);
		String channel = MapUtil.getStr(checkMap, Constant.FieldKey.CHANNEL.code());
		channel = StrUtil.isEmptyIfStr(transAmt) ? Constant.ConstantUtil.DEF_CHANNEL : channel;
		// FTP下载文件到本地目录
		String localFileName = "";
		if (!"0190".equals(channel)) {
			// 解密文件内容 DESUtil.decrypt
		}
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
		LOGGER.info("review--->params is [{}]", context);
		Assert.notNull(context, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
		String batchGroupId = MapUtil.getStr(context, Constant.FieldKey.BATCH_GROUP_ID.code());
		String internalAccount = MapUtil.getStr(context, "internalAccount");
		String batchType = MapUtil.getStr(context, Constant.FieldKey.BATCH_TYPE.code());
		String bizType = MapUtil.getStr(context, Constant.FieldKey.BIZ_TYPE.code());

		String chanlSerialNo = MapUtil.getStr(context, Constant.FieldKey.CHANL_SERIAL_NO.code());
		//// 复核结果:0：同意;1：拒绝
		String checkType = MapUtil.getStr(context, "checkType");

		Assert.notBlank(batchGroupId, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));

		BatchProcessGroup bpg = new BatchProcessGroup();
		bpg.setBatchGroupId(batchGroupId);
		List<BatchProcessGroup> bpdList = batchProcessService.getBatchProcessGroup(bpg);
		Assert.isFalse(CollUtil.isEmpty(bpdList),
				() -> new BusinessException(StatusCode.RTN_NULL, "输入批次头号[" + batchGroupId + "]不正确"));
		bpg = bpdList.get(0);
		Assert.notNull(bpg, () -> new BusinessException(StatusCode.RTN_NULL, "输入批次头号[" + batchGroupId + "]不正确"));
		if (Constant.BatchType.SUE_BAP_OUT.code().equals(batchType)) {
			Assert.notBlank(internalAccount, () -> new BusinessException(StatusCode.PARAMS_CHECK_NULL));
			// 校验账户类型及余额
			// 同意时销客户购买重要凭证 cancelCustomerVoucher

			// 当为往账定期贷记时，需要将前台输入扣款内部账记入批次头里
			// BatchProcessGroup
		}

		// 查询出批次头对应所有已登记批次号
		BatchProcess bp = new BatchProcess();
		bp.setBatchGroupId(batchGroupId);
		bp.setProcessStatus(Constant.BatchStatus.REGISTER.code());
		bp.setBizType(bizType);
		List<BatchProcess> bpList = batchProcessService.getBatchProcessList(bp);
		Assert.notNull(CollUtil.isEmpty(bpList), () -> new BusinessException(StatusCode.RTN_NULL, "没有查询到对应的批次信息"));
		bpList.stream().forEach(baP -> {
			baP.setChanlSerialNo(chanlSerialNo);
			if ("0".equals(checkType)) {// 复核通过
				batchProcessService.updateBatchProcess(baP);// 新事务提交
				// dealBatch
				ServiceDispatcher.runAsync(Constant.ServiceName.DEAL_BATCH_SRV.code(),
						MapUtil.of(Constant.FieldKey.BATCH_ID.code(), baP.getBatchId()));
			} else if ("1".equals(checkType)) {// 复核拒绝
				baP.setProcessStatus(Constant.BatchStatus.CANCEL.code());
				batchProcessService.updateBatchProcess(baP);// 新事务提交
			}
		});
		return MapUtil.of("returnType", "S");
	}
}