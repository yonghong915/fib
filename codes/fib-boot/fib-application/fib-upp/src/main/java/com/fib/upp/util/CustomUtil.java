package com.fib.upp.util;

import java.time.LocalDateTime;

public class CustomUtil {

	/**
	 * 获取报文标识号
	 * 报文标识号的编号规则为：当前工作日期（8位数字）+报文序号（8位数字，不足8位的，左补0）组成，共16位长度，例如2010020200000001。
	 * 
	 * @return
	 */
	public static String getMessageId(String cnapsSystemDate) {
//		if (StrUtil.isBlank(cnapsSystemDate)) {
//			cnapsSystemDate = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
//		}
		// TODO 或取序号//可以取数据库自增序列-Seq_MessageId
		long workerId = 1;
//		Snowflake snowflake = IdUtil.createSnowflake(workerId, 2);
//		String id = snowflake.nextIdStr();
//		if (id.length() > 8) {
//			id = StrUtil.sub(id, id.length() - 8, id.length());
//		}
		//return cnapsSystemDate + id;
		return cnapsSystemDate;
	}

	/**
	 * 报文重账检查规则:CNAPS2使用｛CNAPS2子系统号+发起参与机构+报文标识号｝三项作为业务层面报文重账的检查标准。
	 * 注：对于二代“即时转账报文”，当借贷双方为同一直接参与机构时，该直接参与机构会收到两条三要素相同的二代“即时转账报文”，其中“借贷标识”不同。
	 * 
	 * @return
	 */
	public boolean checkDuplicateMessage() {
		boolean repeatFlag = false;
		// 系统号（通道）
		String systemCode = Constant.SystemCode.HVPS.code();
		// 报文发起参与机构
		String partyIdFrom = "";
		// 报文标识号
		String messageId = "";

		//
		String messageType = "";

		// 查询COMMUNICATION_EVENT查询是否存在

		return repeatFlag;
	}
}
