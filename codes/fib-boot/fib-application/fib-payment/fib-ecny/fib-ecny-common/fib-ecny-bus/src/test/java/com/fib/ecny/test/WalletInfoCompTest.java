package com.fib.ecny.test;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.common.bus.base.WalletInfoComp;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WalletInfoCompTest {
    @Resource
    private WalletInfoComp walletInfoComp;

    @Test
    public void testFindUserById() {
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        String walletId = IdUtil.getSnowflakeNextIdStr();
        walletInfoDto.setWalletId(walletId);
        walletInfoDto.setEcnyCustNo(walletId + RandomUtil.randomNumbers(2));
        walletInfoDto.setWalletName("钱包" + walletId);
        walletInfoDto.setWalletLevel("WL01");
        //walletInfoDto.setWalletStatus("WS01");

        int row = walletInfoComp.updateWalletInfo(walletInfoDto);
        System.out.println(row);
//        walletInfoComp.saveWalletInfo(walletInfoDto);
//        walletInfoDto = walletInfoComp.getWalletInfo(walletInfoDto);
    }
}
