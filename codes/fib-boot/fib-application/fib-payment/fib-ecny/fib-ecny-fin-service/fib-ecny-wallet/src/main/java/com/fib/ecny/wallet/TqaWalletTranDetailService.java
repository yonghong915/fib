package com.fib.ecny.wallet;

import com.fib.ecny.base.dto.wallet.TqaWalletTranDetailDto;
import com.fib.ecny.base.enums.EcnyErrorCode;
import com.fib.ecny.base.exception.BizException;
import com.fib.ecny.mapper.TqaWalletTranDetailMapper;
import com.fib.ecny.wallet.converter.TqaWalletTranDetailConverter;
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
