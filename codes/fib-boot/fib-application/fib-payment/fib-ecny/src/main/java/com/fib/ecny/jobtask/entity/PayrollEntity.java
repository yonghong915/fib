package com.fib.ecny.jobtask.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("batch_pay_roll")
public class PayrollEntity {
    @TableId("batch_id")
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
