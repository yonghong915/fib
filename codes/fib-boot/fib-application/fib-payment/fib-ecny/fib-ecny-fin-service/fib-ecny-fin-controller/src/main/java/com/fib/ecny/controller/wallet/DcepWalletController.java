package com.fib.ecny.controller.wallet;

import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.controller.UwapBaseController;
import com.fib.ecny.base.dto.UwapRequest;
import com.fib.ecny.base.dto.UwapResponse;
import com.fib.ecny.base.dto.dcep.dcep047800101.Dcep047800101;
import com.fib.ecny.wallet.service.dcep.Ecnydl0478Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DcepWalletController extends UwapBaseController {
    private IBizService ecnydl0478Service;

    public DcepWalletController(IBizService ecnydl0478Service) {
        this.ecnydl0478Service = ecnydl0478Service;
    }

    @PostMapping(value = "/ecnydl0478", produces = MediaType.APPLICATION_JSON_VALUE)
    public UwapResponse<?> ecnydl0478(@RequestBody UwapRequest<Dcep047800101> req) {
        return executor(req, Ecnydl0478Service.class, ecnydl0478Service,true);
    }
}
