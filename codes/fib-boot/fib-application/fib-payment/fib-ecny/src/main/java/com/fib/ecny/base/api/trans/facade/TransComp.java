package com.fib.ecny.base.api.trans.facade;

import com.fib.ecny.base.api.trans.dto.TransDto;

public interface TransComp {
    TransDto queryTransById(String transId);
}
