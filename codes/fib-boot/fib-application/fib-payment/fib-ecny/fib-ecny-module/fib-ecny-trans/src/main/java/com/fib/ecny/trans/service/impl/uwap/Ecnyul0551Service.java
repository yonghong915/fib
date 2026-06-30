package com.fib.ecny.trans.service.impl.uwap;

import com.fib.common.bus.base.*;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.trans.comp.TransCommService;
import com.fib.ecny.trans.controller.Uwap050100302Req;
import com.fib.ecny.trans.controller.Uwap050100302Rsp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Ecnyul0551Service implements IBizService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ecnyul0551Service.class);
    @Resource
    private TransCommService transCommService;

    @Override
    public RespEntity<?> arrange(BizContext bizContext) {
        LOGGER.debug("======Ecnyul0551Service======");
        UwapRequest<Uwap050100302Req> uwapRequest = bizContext.getRequest();
        Uwap050100302Req uwap050100302Req = uwapRequest.getMsgBody();
        String walletId = "";
        String ecnyCustNo = "";
        WalletInfoDto walletInfoDto = transCommService.get(walletId, ecnyCustNo);
        Uwap050100302Rsp uwap050100302Rsp = new Uwap050100302Rsp();
        uwap050100302Rsp.setWalletInfoDto(walletInfoDto);
        return RespEntity.ok(uwap050100302Rsp);
    }
}
