package com.fib.ecny.wallet.service;

import com.fib.ecny.base.api.trans.dto.TqaWalletTranDetailDto;
import com.fib.ecny.base.api.trans.facade.ITqaWalletTranDetailService;
import com.fib.ecny.common.util.ValidatorUtils;
import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.api.facade.WalletInfoComp;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {
    private final ITqaWalletTranDetailService tqaWalletTranDetailService;

    private final WalletInfoComp walletInfoComp;

    public WalletService(ITqaWalletTranDetailService tqaWalletTranDetailService, WalletInfoComp walletInfoComp) {
        this.tqaWalletTranDetailService = tqaWalletTranDetailService;
        this.walletInfoComp = walletInfoComp;
    }

    public int saveWallet(WalletInfoDto walletInfoDto) {
        TqaWalletTranDetailDto tqaWalletTranDetailDto = new TqaWalletTranDetailDto();
        ValidatorUtils.validate(tqaWalletTranDetailDto);
        int affectedRows = tqaWalletTranDetailService.saveTqaWalletTranDetail(tqaWalletTranDetailDto);
        int rows = walletInfoComp.saveWalletInfo(walletInfoDto);
        Optional<WalletInfoDto> ss = walletInfoComp.getWalletInfo(walletInfoDto);
        ss.ifPresent(val -> System.out.println(val.getEcnyCustNo()));
        return rows;
    }
}
