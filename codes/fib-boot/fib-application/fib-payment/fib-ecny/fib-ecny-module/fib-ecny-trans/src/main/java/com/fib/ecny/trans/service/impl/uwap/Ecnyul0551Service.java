package com.fib.ecny.trans.service.impl.uwap;

import com.fib.common.bus.base.BizContext;
import com.fib.common.bus.base.IBusiService;
import com.fib.common.bus.base.RespEntity;
import com.fib.common.bus.base.UwapRequest;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.common.bus.base.WalletInfoComp;
import com.fib.ecny.trans.controller.Uwap050100302Req;
import com.fib.ecny.trans.controller.Uwap050100302Rsp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Ecnyul0551Service implements IBusiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ecnyul0551Service.class);
    @Resource
    private WalletInfoComp walletInfoComp;

    @Override
    public RespEntity arrange(BizContext bizContext) {
        LOGGER.debug("======Ecnyul0551Service======");
        UwapRequest<Uwap050100302Req> uwapRequest = bizContext.getRequest();
        Uwap050100302Req uwap050100302Req = uwapRequest.getMsgBody();
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        walletInfoDto.setWalletId("201233444");
        WalletInfoDto retWalletInfo = walletInfoComp.getWalletInfo(walletInfoDto);

        Uwap050100302Rsp uwap050100302Rsp = new Uwap050100302Rsp();
        uwap050100302Rsp.setWalletInfoDto(walletInfoDto);
        return RespEntity.ok(uwap050100302Rsp);
    }
}
