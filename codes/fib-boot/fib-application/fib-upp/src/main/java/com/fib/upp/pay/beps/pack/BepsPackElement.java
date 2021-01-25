package com.fib.upp.pay.beps.pack;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@Data
public class BepsPackElement {
	/*
	 * 业务类型
	 */
	private String transactionType;
	/*
	 * 发起清算行
	 */
	private String sendClearingBank;
	/*
	 * 接收清算行
	 */
	private String receiveClearingBank;
	/*
	 * 相同的回执期
	 */
	private String returnLimited;
	/*
	 * 原报文标识号
	 */
	private String originalMessageId;
	/*
	 * 批次号
	 */
	private String batchId;

	/*
	 * 包序号
	 */
	private int packNO = 0;
}
