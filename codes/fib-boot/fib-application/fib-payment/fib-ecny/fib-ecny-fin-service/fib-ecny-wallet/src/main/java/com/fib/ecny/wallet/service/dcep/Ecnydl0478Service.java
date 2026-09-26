package com.fib.ecny.wallet.service.dcep;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.dto.BizContext;
import com.fib.ecny.base.dto.RespEntity;
import com.fib.ecny.base.dto.dcep.dcep047800101.Dcep047800101;
import com.fib.ecny.base.dto.wallet.WalletInfoDto;
import com.fib.ecny.wallet.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        return RespEntity.ok(new Dcep047800101());
    }
}
