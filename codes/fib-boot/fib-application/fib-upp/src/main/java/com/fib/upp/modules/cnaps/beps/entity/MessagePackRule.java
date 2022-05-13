package com.fib.upp.modules.cnaps.beps.entity;

import java.math.BigInteger;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fib.core.base.dto.BaseDTO;
import com.fib.upp.util.BitStateUtil;

/**
 * 报文组包规则
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-22 11:10:51
 */
public class MessagePackRule extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5455320887301852653L;

	/** 主键 */
	@TableId("pk_id")
	private BigInteger pkId;

	/** 报文类型 */
	private String messageTypeCode;

	/**
	 * 组包规则,二进制对应的十进制值
	 * <p>
	 * 0000 0000 0000 0001 transactionType 交易类型
	 * </p>
	 * <p>
	 * 0000 0000 0000 0010 sendClearingBank 发送清算行
	 * </p>
	 * <p>
	 * 0000 0000 0000 0100 receiveClearingBank 接收清算行
	 * </p>
	 * <p>
	 * 0000 0000 0000 1000 returnLimited 回执
	 * </p>
	 * <p>
	 * 0000 0000 0001 0000 originalMessageId 原报文标识号
	 * </p>
	 * <p>
	 * 0000 0000 0010 0000 batchId 批次号
	 * </p>
	 */
	private Integer ruleValue;

	private static final long TRANSACTION_TYPE = 1 << 0;
	private static final long SEND_CLEARING_BANK = 1 << 1;
	private static final long RECEIVE_CLEAR_BANK = 1 << 2;
	private static final long RETURN_LIMITED = 1 << 3;
	private static final long ORIGINAL_MESSAGE_ID = 1 << 4;
	private static final long BATCH_ID = 1 << 5;

	public boolean transactionType() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), TRANSACTION_TYPE);
	}

	public boolean sendClearingBank() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), SEND_CLEARING_BANK);
	}

	public boolean receiveClearBank() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), RECEIVE_CLEAR_BANK);
	}

	public boolean returnLimited() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), RETURN_LIMITED);
	}

	public boolean originalMessageId() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), ORIGINAL_MESSAGE_ID);
	}

	public boolean batchId() {
		return Objects.nonNull(ruleValue)
				&& BitStateUtil.has(Long.parseLong(Integer.toBinaryString(ruleValue)), BATCH_ID);
	}

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public Integer getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(Integer ruleValue) {
		this.ruleValue = ruleValue;
	}

	public String getPackStr(MessagePackElement packElem) {
		StringBuilder sb = new StringBuilder();
		if (transactionType()) {
			sb.append(String.format("%1$-6s", getTrim(packElem.getTransactionType())));
		}

		// 发起清算行
		if (sendClearingBank()) {
			sb.append(String.format("%1$-14s", getTrim(packElem.getSendClearingBank())));
		}
		// 接收清算行
		if (receiveClearBank()) {
			sb.append(String.format("%1$-14s", getTrim(packElem.getReceiveClearingBank())));
		}
		// 相同的回执期
		if (returnLimited()) {
			sb.append(String.format("%1$-2s", getTrim(packElem.getReturnLimited())));
		}
		// 原报文标识号:当前工作日期（8位数字）+报文序号（8位数字，不足8位的，左补0）组成，共16位长度，例如2010020200000001
		if (originalMessageId()) {
			sb.append(String.format("%1$-16s", getTrim(packElem.getOriginalMessageId())));
		}
		// 批次号
		if (batchId()) {
			sb.append(String.format("%1$-20s", getTrim(packElem.getBatchId())));
		}
		// 包序号，默认为零
		sb.append(String.format("%1$-6s", "pkg" + packElem.getPackNO()));

		return sb.toString();
	}

	public static String getTrim(String str) {
		if (null != str) {
			return str.trim();
		}
		return "";
	}
}