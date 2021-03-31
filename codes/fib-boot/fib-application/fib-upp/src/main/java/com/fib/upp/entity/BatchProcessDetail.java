package com.fib.upp.entity;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
@Data
public class BatchProcessDetail {
	private String batchId;
	private String batchSeqId;
	private String payeeAccNo;
	private String payeeAccName;
	private String payeeAddress;
	private String payeeBankCode;
	private String payeeBankName;
}
