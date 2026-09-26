package com.fib.ecny.trans.service.uwap;

import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.dto.BizContext;
import com.fib.ecny.base.dto.RespEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Ecnyul0501Service implements IBizService {
    @Override
    public RespEntity arrange(BizContext context) {
        log.info("enter Ecnyul0501Service begin");
        return new RespEntity();
    }
}
