package com.fib.ecny.trans.controller;

import com.fib.common.bus.base.UwapRequest;
import com.fib.ecny.trans.service.impl.uwap.Ecnyul0551Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UwapController extends UwapBaseController {

    @PostMapping(value = "/ecnyul0501", produces = MediaType.APPLICATION_JSON_VALUE)
    public UwapResponse<?> ecnyul0501(@RequestBody UwapRequest<Uwap050100302Req> req) {
        return executor(req, Ecnyul0551Service.class);
    }
}