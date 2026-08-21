package com.fib.ecny.wallet.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.base.BizContext;
import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.RespEntity;
import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.controller.Dcep047800101;
import org.springframework.stereotype.Service;

@Service
public class Ecnydl0478Service implements IBizService {

    private final WalletService walletService;

    public Ecnydl0478Service(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public RespEntity arrange(BizContext context) {
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        String walletId = IdUtil.getSnowflakeNextIdStr();
        walletInfoDto.setWalletId(walletId);
        walletInfoDto.setEcnyCustNo(walletId + RandomUtil.randomNumbers(2));
        walletInfoDto.setWalletName("钱包" + walletId);
        walletInfoDto.setWalletLevel("WL01");
        walletInfoDto.setWalletStatus("WS01");
        walletInfoDto.setRemark("ok");

        int row = walletService.saveWallet(walletInfoDto);
        // int row = walletInfoComp.updateWalletInfo(walletInfoDto);
        System.out.println(row);

        ;

        return RespEntity.ok(new Dcep047800101());
    }
}
