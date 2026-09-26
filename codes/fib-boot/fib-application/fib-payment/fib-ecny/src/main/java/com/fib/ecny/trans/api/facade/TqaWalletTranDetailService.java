package com.fib.ecny.trans.api.facade;

import com.fib.ecny.base.api.trans.dto.TqaWalletTranDetailDto;
import com.fib.ecny.base.api.trans.facade.ITqaWalletTranDetailService;
import com.fib.ecny.base.enums.service.EcnyErrorCode;
import com.fib.ecny.common.exception.BizException;
import com.fib.ecny.trans.converter.TqaWalletTranDetailConverter;
import com.fib.ecny.trans.mapper.TqaWalletTranDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("tqaWalletTranDetailService")
public class TqaWalletTranDetailService implements ITqaWalletTranDetailService {
    private final TqaWalletTranDetailMapper tqaWalletTranDetailMapper;

    public TqaWalletTranDetailService(TqaWalletTranDetailMapper tqaWalletTranDetailMapper) {
        this.tqaWalletTranDetailMapper = tqaWalletTranDetailMapper;
    }

    @Override
    public int saveTqaWalletTranDetail(TqaWalletTranDetailDto tqaWalletTranDetailDto) {
        try {
            return tqaWalletTranDetailMapper.insert(TqaWalletTranDetailConverter.INSTANCE.fromDto(tqaWalletTranDetailDto));
        } catch (Exception e) {
            log.error("Failed to execute to save TqaWalletTranDetail", e);
            throw new BizException(EcnyErrorCode.DB_EXP);
        }
    }
}
