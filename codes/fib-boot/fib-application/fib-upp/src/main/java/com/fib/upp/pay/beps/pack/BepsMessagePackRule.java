package com.fib.upp.pay.beps.pack;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.core.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 二代小额支付系统业务组包规则
 * <p>
 * a)普通贷记业务、普通借记业务、实时贷记业务、实时借记业务、CIS通用回执业务、信息类业务按接收清算行分别组包。
 * </p>
 * <p>
 * b)定期贷记业务和定期借记业务根据业务类型号按接收清算行分别组包。支付系统为每一业务类型分配唯一的标识代码。业务种类是标识每笔明细业务的附加性质，如定期贷记或定期借记业务的水费、电费、煤气费等，是对业务类型包括内容的具体说明。
 * </p>
 * <p>
 * c)实时贷记业务包、实时借记业务包、实时代收业务包、实时代付业务包和实时信息包每包限一笔。
 * </p>
 * <p>
 * d)借记业务按相同的回执期（T+N）组包。
 * </p>
 * <p>
 * e)集中代收付中心发起的批量代收业务，按同一收款人、同一业务类型号、同一发起清算行、同一接收清算行、同一回执期限组包；批量代付业务按同一付款人、同一业务类型号、同一发起清算行、同一接收清算行、同一回执期限组包。（集中代收付中心根据收（付）款单位分别组包更便于中心与收（付）款单位信息传输服务，更符合业务开展实际情况。）
 * </p>
 * <p>
 * f)借记回执包按原包组包，并附加成功或失败的信息。
 * </p>
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "beps_message_pack_rule")
public class BepsMessagePackRule extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6086892518801230052L;
	private String messageType;
	/*
	 * 包的'业务类型'都需一致
	 */
	private boolean transactionType;
	/*
	 * 包的'发起清算行'都需一致
	 */
	private boolean sendClearingBank;
	/*
	 * 包的'接收清算行'都需一致
	 */
	private boolean receiveClearingBank;
	/*
	 * 包的'相同的回执期'都需一致
	 */
	private boolean returnLimited;
	/*
	 * 包的'原报文标识号'都需一致
	 */
	private boolean originalMessageId;
	/*
	 * 包的'批次号'都需一致
	 */
	private boolean batchId;

	/*
	 * 允许参与者数量目前最大为2000个
	 */
	public static final int maxPartnerNum = 2000;

	/**
	 * 将小额组包要素拼成字符串，在小额组包时，相同组包要素字符串代表的订单记录将打成一个小额包发送。
	 * 
	 * @param elem
	 * @return
	 */
	public String getPackString(BepsPackElement elem) {
		StringBuilder builder = new StringBuilder();
		// 业务类型
//		if (transactionType) {
//			builder.append(String.format("%1$-6s", StrUtil.trimToEmpty(elem.getTransactionType())));
//		}
//		// 发起清算行
//		if (sendClearingBank) {
//			builder.append(String.format("%1$-14s", StrUtil.trimToEmpty(elem.getSendClearingBank())));
//		}
//		// 接收清算行
//		if (receiveClearingBank) {
//			builder.append(String.format("%1$-14s", StrUtil.trimToEmpty(elem.getReceiveClearingBank())));
//		}
//		// 相同的回执期
//		if (returnLimited) {
//			builder.append(String.format("%1$-2s", StrUtil.trimToEmpty(elem.getReturnLimited())));
//		}
//		// 原报文标识号:当前工作日期（8位数字）+报文序号（8位数字，不足8位的，左补0）组成，共16位长度，例如2010020200000001
//		if (originalMessageId) {
//			builder.append(String.format("%1$-16s", StrUtil.trimToEmpty(elem.getOriginalMessageId())));
//		}
//		// 批次号
//		if (batchId) {
//			builder.append(String.format("%1$-20s", StrUtil.trimToEmpty(elem.getBatchId())));
//		}
		// 包序号，默认为零
		builder.append(String.format("%1$-6s", "pkg" + elem.getPackNO()));
		return builder.toString();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		// 业务类型
		builder.append("transactionType=" + transactionType);
		// 发起清算行
		builder.append(" sendClearingBank=" + sendClearingBank);
		// 接收清算行
		builder.append(" receiveClearingBank=" + receiveClearingBank);
		// 相同的回执期
		builder.append(" returnLimited=" + returnLimited);
		// 原报文标识号
		builder.append(" originalMessageId=" + originalMessageId);
		// 批次号
		builder.append(" originalMessageId=" + originalMessageId);

		return builder.toString();
	}
}
