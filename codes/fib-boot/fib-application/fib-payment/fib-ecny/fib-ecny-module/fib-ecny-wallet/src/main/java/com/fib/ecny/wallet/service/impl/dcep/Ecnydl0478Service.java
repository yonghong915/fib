package com.fib.ecny.wallet.service.impl.dcep;

import com.fib.common.bus.base.BizContext;
import com.fib.common.bus.base.IBizService;
import com.fib.common.bus.base.RespEntity;
import com.fib.common.bus.base.UwapRequest;
import com.fib.common.bus.dcep.dcep047800101.Dcep047800101;
import com.fib.common.bus.dcep.dcep047800101.Dcep047900101;
import com.fib.ecny.common.bus.base.WalletInfoComp;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Ecnydl0478Service implements IBizService {
    @Resource
    private WalletInfoComp walletInfoComp;

    @Override
    public RespEntity arrange(BizContext bizContext) {
        log.debug("======Ecnydl0478Service======");
        UwapRequest<Dcep047800101> uwapRequest = bizContext.getRequest();
        Dcep047800101 dcep047800101 = uwapRequest.getMsgBody();
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        walletInfoDto.setWalletId(dcep047800101.getWalletId());
        WalletInfoDto retWalletInfo = walletInfoComp.getWalletInfo(walletInfoDto);

        walletInfoComp.saveWalletInfo(walletInfoDto);

        Dcep047900101 dcep047900101 = new Dcep047900101();

        return RespEntity.ok(dcep047900101);
    }
}
