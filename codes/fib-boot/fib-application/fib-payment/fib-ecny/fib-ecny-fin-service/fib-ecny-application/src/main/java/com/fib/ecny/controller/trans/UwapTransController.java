package com.fib.ecny.controller.trans;

import com.fib.ecny.base.controller.UwapBaseController;
import com.fib.ecny.base.dto.UwapRequest;
import com.fib.ecny.base.dto.UwapResponse;
import com.fib.ecny.trans.service.uwap.Ecnyul0501Service;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UwapTransController extends UwapBaseController {
    @PostMapping(value = "/ecnyul0501", produces = MediaType.APPLICATION_JSON_VALUE)
    public UwapResponse<?> ecnyul0501(@RequestBody UwapRequest<?> req) {
        return executor(req,Ecnyul0501Service.class);
    }
}
