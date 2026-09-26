package com.fib.ecny.jobtask;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayrollDto {
    private Long batchId;
    private String batchName;
    private String status;
    private Integer totalCnt;
    private BigDecimal totalAmt;
    private Integer successCnt;
    private BigDecimal successAmt;
    private Integer failCnt;
    private BigDecimal failAmt;
}
