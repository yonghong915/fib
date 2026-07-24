package com.fib.ecny.common.bus.vo.wallet;

import lombok.Data;

@Data
public class WalletInfoVo {
    private String ecnyCustNo;
    private String walletId;
    private String walletName;
    private String walletLevel;
    private String walletStatus;
    private String remark;
}
