package com.fib.ecny.base.enums;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TqaMainSerial {
    private BigInteger id;
    private TranStatusEnum tranStatusEnum;
}
