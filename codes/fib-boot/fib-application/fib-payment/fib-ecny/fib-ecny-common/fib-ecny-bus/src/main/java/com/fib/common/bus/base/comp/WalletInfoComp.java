package com.fib.common.bus.base.comp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fib.common.bus.base.BizException;
import com.fib.common.bus.base.annotation.ErrorCode;
import com.fib.common.bus.base.comp.converter.WalletInfoStructMapperConverter;
import com.fib.common.bus.base.comp.dto.WalletInfoDto;
import com.fib.entity.WalletInfoEntity;
import com.fib.mapper.WalletInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletInfoComp {
    private final WalletInfoMapper walletInfoMapper;

    public WalletInfoComp(WalletInfoMapper walletInfoMapper) {
        this.walletInfoMapper = walletInfoMapper;
    }

    public WalletInfoDto getWalletInfo(WalletInfoDto walletInfoDto) {
        LambdaQueryWrapper<WalletInfoEntity> wrapper = Wrappers.lambdaQuery(WalletInfoStructMapperConverter.INSTANCE.fromDto(walletInfoDto));
        try {
            return WalletInfoStructMapperConverter.INSTANCE.toDto(walletInfoMapper.selectOne(wrapper, Boolean.FALSE));
        } catch (Exception e) {
            //log.error("Failed to execute to query walletInfo.", e);
            throw new BizException(ErrorCode.FAIL);
        }
    }
}