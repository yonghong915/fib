package com.fib.ecny.common.bus.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fib.common.bus.base.BizException;
import com.fib.common.bus.base.annotation.ErrorCode;
import com.fib.ecny.common.bus.converter.wallet.WalletInfoMapperConverter;
import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.entity.WalletInfoEntity;
import com.fib.ecny.mapper.WalletInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WalletInfoComp {
    private static final Logger LOGGER =  LoggerFactory.getLogger(WalletInfoComp.class);
    private final WalletInfoMapper walletInfoMapper;

    public WalletInfoComp(WalletInfoMapper walletInfoMapper) {
        this.walletInfoMapper = walletInfoMapper;
    }

    public WalletInfoDto getWalletInfo(WalletInfoDto walletInfoDto) {
        LambdaQueryWrapper<WalletInfoEntity> wrapper = Wrappers.lambdaQuery(WalletInfoMapperConverter.INSTANCE.fromDto(walletInfoDto));
        try {
            return WalletInfoMapperConverter.INSTANCE.toDto(walletInfoMapper.selectOne(wrapper, Boolean.FALSE));
        } catch (Exception e) {
            LOGGER.error("Failed to execute to query walletInfo.", e);
            throw new BizException(ErrorCode.FAIL);
        }
    }
}