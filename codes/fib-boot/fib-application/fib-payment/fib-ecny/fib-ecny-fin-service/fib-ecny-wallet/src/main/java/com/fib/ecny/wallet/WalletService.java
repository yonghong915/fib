package com.fib.ecny.wallet;

import com.fib.ecny.base.dto.wallet.TqaWalletTranDetailDto;
import com.fib.ecny.base.dto.wallet.WalletInfoDto;
import com.fib.ecny.base.util.ValidatorUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("walletService")
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