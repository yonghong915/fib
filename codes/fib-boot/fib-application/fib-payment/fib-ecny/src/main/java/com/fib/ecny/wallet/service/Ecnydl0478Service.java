package com.fib.ecny.wallet.service;

import com.fib.ecny.base.BizContext;
import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.RespEntity;
import com.fib.ecny.wallet.api.facade.WalletInfoComp;
import org.springframework.stereotype.Service;

@Service
public class Ecnydl0478Service implements IBizService {

    private final WalletService walletService;

    public Ecnydl0478Service(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public RespEntity arrange(BizContext context) {
        walletService.saveWallet();;

        return null;
    }
}
