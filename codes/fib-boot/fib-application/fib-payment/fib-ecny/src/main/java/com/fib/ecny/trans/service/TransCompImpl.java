package com.fib.ecny.trans.service;

import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.base.api.trans.dto.TransDto;
import com.fib.ecny.base.api.trans.facade.TransComp;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("transCompImpl")
public class TransCompImpl implements TransComp {
    @Override
    public TransDto queryTransById(String transId) {
        TransDto transDto = new TransDto();
        transDto.setTransId(transId);
        transDto.setTransName("123333");
        transDto.setTransAmt(new BigDecimal(RandomUtil.randomNumbers(6)));
        return transDto;
    }
}
