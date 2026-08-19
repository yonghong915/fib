package com.fib.ecny.wallet.service;

import com.fib.ecny.base.api.trans.dto.TransDto;
import com.fib.ecny.base.api.trans.facade.TransComp;
import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.api.facade.WalletInfoComp;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {
    private final TransComp transCompImpl;

    private final WalletInfoComp walletInfoComp;

    public WalletService(TransComp transCompImpl, WalletInfoComp walletInfoComp) {
        this.transCompImpl = transCompImpl;
        this.walletInfoComp = walletInfoComp;
    }

    public void saveWallet() {
        TransDto transDto = transCompImpl.queryTransById("12344");
        System.out.println(transDto.getTransAmt());
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        Optional<WalletInfoDto> ss = walletInfoComp.getWalletInfo(walletInfoDto);

    }
}
