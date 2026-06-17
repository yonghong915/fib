package com.fib.ecny.trans.controller;

import com.fib.common.bus.base.BaseController;
import com.fib.common.bus.base.BizContext;
import com.fib.common.bus.base.RespEntity;
import com.fib.ecny.trans.service.impl.uwap.Ecnyul0551Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UwapBaseController extends BaseController {

    @PostMapping("/ecnyul0501")
    public RespEntity ecnyul0501(Uwap0501Req uwap0501Req){
        return executor(Ecnyul0551Service.class,uwap0501Req);
    }
}
