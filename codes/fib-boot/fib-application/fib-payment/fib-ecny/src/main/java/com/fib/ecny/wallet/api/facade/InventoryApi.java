package com.fib.ecny.wallet.api.facade;

import com.fib.ecny.wallet.api.dto.WalletDto;

import org.springframework.stereotype.Component;


@Component
public class InventoryApi {
    public WalletDto saveWallet(){
        WalletDto walletDto = new WalletDto();
        return walletDto;
    }
    public void reserve(String sku, int qty) { /* ... */ }
}
