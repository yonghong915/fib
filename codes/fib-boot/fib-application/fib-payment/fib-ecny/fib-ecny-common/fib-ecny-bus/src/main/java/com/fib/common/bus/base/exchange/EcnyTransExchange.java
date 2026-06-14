package com.fib.common.bus.base.exchange;

import com.fib.common.bus.base.exchange.trans.RedeemAcctReq;
import com.fib.common.bus.base.exchange.trans.RedeemAcctRsp;
import jakarta.validation.Valid;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/trans")
public interface EcnyTransExchange {

    @PostExchange("/ecnyReddemAcct")
    RedeemAcctRsp ecnyReddemAcct(@Valid RedeemAcctReq redeemAcctReq);
}
