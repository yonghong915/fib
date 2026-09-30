package com.fib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettlementOrder {
    private String acctNo;
    private Long amt;
}
