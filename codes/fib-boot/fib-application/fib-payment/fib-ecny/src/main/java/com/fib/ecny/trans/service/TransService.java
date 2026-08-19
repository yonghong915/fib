package com.fib.ecny.trans.service;

import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.api.facade.InventoryApi;
import org.springframework.stereotype.Service;

@Service
public class TransService {
    private InventoryApi inventoryApi;

    public TransService(InventoryApi inventoryApi) {
        this.inventoryApi = inventoryApi;
    }

    public WalletInfoDto saveWallet() {
        return inventoryApi.saveWallet();
    }

    public void saveTrans() {
        inventoryApi.reserve("aaa", 2);
    }
}
