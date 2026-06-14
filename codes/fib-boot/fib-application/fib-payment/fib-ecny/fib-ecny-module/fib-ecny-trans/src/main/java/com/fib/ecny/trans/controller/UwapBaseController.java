package com.fib.ecny.trans.controller;

import com.fib.common.bus.base.BaseController;
import com.fib.common.bus.base.BizContext;
import com.fib.common.bus.base.RespEntity;
import com.fib.ecny.trans.service.impl.uwap.Ecnyul0551Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class UwapBaseController extends BaseController {

    @PostMapping("/ecnyul0501")
    public RespEntity ecnyul0501(BizContext bizContext) {
        return execute(Ecnyul0551Service.class, bizContext);
    }
}
