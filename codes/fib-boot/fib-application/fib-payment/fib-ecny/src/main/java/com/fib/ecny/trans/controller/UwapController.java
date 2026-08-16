package com.fib.ecny.trans.controller;

import com.fib.ecny.base.UwapBaseController;
import com.fib.ecny.base.UwapRequest;
import com.fib.ecny.base.UwapResponse;
import com.fib.ecny.trans.service.Ecnyul0501Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UwapController extends UwapBaseController {
    @PostMapping(value = "/ecnyul0501", produces = MediaType.APPLICATION_JSON_VALUE)
    public UwapResponse<?> ecnyul0501(@RequestBody UwapRequest<?> req) {
        return executor(req, Ecnyul0501Service.class);
    }

    private UwapResponse<?> executor(UwapRequest<?> req, Class<Ecnyul0501Service> ecnyul0501ServiceClass) {
        return new UwapResponse<>();
    }
}
