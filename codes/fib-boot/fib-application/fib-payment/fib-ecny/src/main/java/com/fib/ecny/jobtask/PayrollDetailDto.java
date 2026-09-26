package com.fib.ecny.jobtask;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayrollDetailDto {
    private Long batchId;
    private Long seqId;
    private String payeeWalletId;
    private String payeeWalletLevel;
    private String payeeWalletName;
    private String payeeName;
    private String status;
    private BigDecimal amt;
}
