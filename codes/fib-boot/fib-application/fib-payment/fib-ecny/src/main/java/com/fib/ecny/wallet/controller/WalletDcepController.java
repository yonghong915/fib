package com.fib.ecny.wallet.controller;

import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.UwapRequest;
import com.fib.ecny.base.UwapResponse;
import com.fib.ecny.base.controller.DcepController;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WalletDcepController extends DcepController {
    @Resource
    private IBizService ecnydl0478Service;

    @PostMapping(value = "/ecnydl0478", produces = MediaType.APPLICATION_JSON_VALUE)
    public UwapResponse<?> ecnydl0478(@RequestBody UwapRequest<Dcep047800101> req) {
        return executor(req, ecnydl0478Service);
    }
}
