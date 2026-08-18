package com.fib.ecny.wallet.service;

import com.fib.ecny.base.api.trans.facade.TransComp;
import com.fib.ecny.trans.api.facade.TransApi;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private TransComp transCompImpl;

    public WalletService(TransComp transCompImpl) {
        this.transCompImpl = transCompImpl;
    }

    public void saveWallet() {

    }
}
