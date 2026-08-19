package com.fib.ecny.wallet.api.facade;

import com.fib.ecny.wallet.api.dto.WalletInfoDto;

import org.springframework.stereotype.Component;


@Component
public class InventoryApi {
    public WalletInfoDto saveWallet(){
        WalletInfoDto walletDto = new WalletInfoDto();
        return walletDto;
    }
    public void reserve(String sku, int qty) { /* ... */ }
}
