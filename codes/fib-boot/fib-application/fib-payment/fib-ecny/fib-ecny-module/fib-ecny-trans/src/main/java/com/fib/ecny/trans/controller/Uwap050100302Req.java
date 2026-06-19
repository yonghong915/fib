package com.fib.ecny.trans.controller;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Uwap050100302Req {

    @NotBlank(message = "walletId must not be null.")
    private String walletId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}
