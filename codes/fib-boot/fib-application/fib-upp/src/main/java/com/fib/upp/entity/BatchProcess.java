package com.fib.upp.entity;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
@Data
public class BatchProcess {

	private String batchType;

	private String processStatus;

	private String endDateTime;

	private BigDecimal transactionSum;
}
