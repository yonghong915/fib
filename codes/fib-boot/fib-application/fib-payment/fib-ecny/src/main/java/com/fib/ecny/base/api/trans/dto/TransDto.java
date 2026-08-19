package com.fib.ecny.base.api.trans.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransDto {
    private String transId;
    private String transName;
    private BigDecimal transAmt;
}
