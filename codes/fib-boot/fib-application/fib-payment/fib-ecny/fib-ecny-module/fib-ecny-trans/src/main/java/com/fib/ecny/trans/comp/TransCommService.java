package com.fib.ecny.trans.comp;

import com.fib.common.bus.base.BizException;
import com.fib.common.bus.base.EcnyErrorCode;
import com.fib.ecny.common.bus.base.WalletInfoComp;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TransCommService {
    @Resource
    private WalletInfoComp walletInfoComp;

    /**
     * 通过钱包编码和数币客户号获取钱包信息
     *
     * @param walletId   钱包编码
     * @param ecnyCustNo 数币客户号
     * @return 钱包信息
     */
    public WalletInfoDto get(String walletId, String ecnyCustNo) {
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        walletInfoDto.setWalletId(walletId);
        walletInfoDto.setEcnyCustNo(ecnyCustNo);
        return walletInfoComp.getWalletInfo(walletInfoDto).orElseThrow(() -> new BizException(EcnyErrorCode.UWAP_R_9999));
    }
}
