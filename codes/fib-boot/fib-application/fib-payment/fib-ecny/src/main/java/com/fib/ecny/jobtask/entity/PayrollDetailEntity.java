package com.fib.ecny.jobtask.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("batch_pay_roll_detail")
public class PayrollDetailEntity {
    private Long batchId;
    private Long seqId;
    private String payeeWalletId;
    private String payeeWalletLevel;
    private String payeeWalletName;
    private String payeeName;
    private String status;
    private BigDecimal amt;
}
