package com.fib.ecny.controller.trans;

import com.fib.ecny.base.controller.DcepBaseController;
import com.fib.ecny.base.dto.dcep.dcep022100101.Dcep022100101;
import com.fib.ecny.trans.service.dcep.Ecnydl0221Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class DcepTransController extends DcepBaseController {
    @PostMapping(value = "/ecnydl0221")
    public String ecnydl0221(@RequestBody String request){
        return executor(request, Dcep022100101.class, Ecnydl0221Service.class);
    }
}
