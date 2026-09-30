package com.fib.ecny.module.trans;

import com.fib.ecny.module.trans.dto.RedeemAcctReq;
import com.fib.ecny.module.trans.dto.RedeemAcctRsp;
import jakarta.validation.Valid;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/trans")
public interface EcnyTransExchange {

    @PostExchange("/ecnyRedeemAcct")
    RedeemAcctRsp ecnyRedeemAcct(@Valid RedeemAcctReq redeemAcctReq);
}
