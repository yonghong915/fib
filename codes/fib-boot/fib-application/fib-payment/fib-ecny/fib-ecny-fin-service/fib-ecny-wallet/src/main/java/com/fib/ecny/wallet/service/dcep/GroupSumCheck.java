package com.fib.ecny.wallet.service.dcep;

import lombok.Data;

@Data
public class GroupSumCheck {
    private String groupId;
    private String hash;
    private Long totalDetailedCount;
}
