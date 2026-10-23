package com.fib.ecny.wallet.service.dcep;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RedPacketInfo {
    private Integer redPacketId;
    private BigDecimal redPacketAmt;
}
