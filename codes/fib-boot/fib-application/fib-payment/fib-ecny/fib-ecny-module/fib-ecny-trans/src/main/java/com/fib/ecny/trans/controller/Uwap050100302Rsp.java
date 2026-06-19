package com.fib.ecny.trans.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;

@JsonRootName("Reed")
public class Uwap050100302Rsp {

    private WalletInfoDto walletInfoDto;

    public WalletInfoDto getWalletInfoDto() {
        return walletInfoDto;
    }

    public void setWalletInfoDto(WalletInfoDto walletInfoDto) {
        this.walletInfoDto = walletInfoDto;
    }
}
