package com.fib.ecny.trans.comp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class OrderMessage implements Message {
    private BigDecimal tranAmt;
}
