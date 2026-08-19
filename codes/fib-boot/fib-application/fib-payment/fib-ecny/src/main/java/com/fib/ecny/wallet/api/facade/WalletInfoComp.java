package com.fib.ecny.wallet.api.facade;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fib.ecny.base.service.EcnyErrorCode;
import com.fib.ecny.common.exception.BizException;
import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.converter.WalletInfoConverter;
import com.fib.ecny.wallet.entity.WalletInfoEntity;
import com.fib.ecny.wallet.mapper.WalletInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 钱包信息组件
 */
@Component
@Slf4j
public class WalletInfoComp {
    private final WalletInfoMapper walletInfoMapper;

    public WalletInfoComp(WalletInfoMapper walletInfoMapper) {
        this.walletInfoMapper = walletInfoMapper;
    }

    public Optional<WalletInfoDto> getWalletInfo(WalletInfoDto walletInfoDto) {
        LambdaQueryWrapper<WalletInfoEntity> wrapper = Wrappers.lambdaQuery(WalletInfoEntity.class);
        WalletInfoEntity walletInfoEntity = WalletInfoConverter.INSTANCE.fromDto(walletInfoDto);

        wrapper.eq(WalletInfoEntity::getWalletId, walletInfoEntity.getWalletId());
        wrapper.eq(WalletInfoEntity::getEcnyCustNo, walletInfoEntity.getEcnyCustNo());
        try {
            return Optional.ofNullable(WalletInfoConverter.INSTANCE.toDto(walletInfoMapper.selectOne(wrapper, Boolean.FALSE)));
        } catch (Exception e) {
            log.error("Failed to execute to query walletInfo.", e);
            throw new BizException(EcnyErrorCode.DB_EXP);
        }
    }

    /**
     * 报文钱包信息
     *
     * @param walletInfoDto 钱包信息 DTO
     * @return 影响行数
     */
    public int saveWalletInfo(WalletInfoDto walletInfoDto) {
        try {
            return walletInfoMapper.insert(WalletInfoConverter.INSTANCE.fromDto(walletInfoDto));
        } catch (Exception e) {
            log.error("Failed to execute to save walletInfo", e);
            throw new BizException(EcnyErrorCode.DB_EXP);
        }
    }

    /**
     * 根据数币客户号与钱包编码更新钱包信息
     *
     * @param walletInfoDto 钱包信息 DTO
     * @return 影响行数
     */
    public int updateWalletInfo(WalletInfoDto walletInfoDto) {
        if (StrUtil.isEmpty(walletInfoDto.getEcnyCustNo()) || StrUtil.isEmpty(walletInfoDto.getWalletId())) {
            log.error("==== updateWalletInfo ==== 参数数币客户号或钱包编码不能为空");
            throw new BizException(EcnyErrorCode.E1000002, "数币客户号或钱包编码不能为空");
        }
        LambdaUpdateWrapper<WalletInfoEntity> wrapper = Wrappers.lambdaUpdate(WalletInfoEntity.class);
        WalletInfoEntity walletInfoEntity = WalletInfoConverter.INSTANCE.fromDto(walletInfoDto);

        wrapper.set(StrUtil.isNotEmpty(walletInfoEntity.getWalletName()), WalletInfoEntity::getWalletName, walletInfoEntity.getWalletName());
        wrapper.set(StrUtil.isNotEmpty(walletInfoEntity.getWalletLevel()), WalletInfoEntity::getWalletLevel, walletInfoEntity.getWalletLevel());
        wrapper.set(StrUtil.isNotEmpty(walletInfoEntity.getWalletStatus()), WalletInfoEntity::getWalletStatus, walletInfoEntity.getWalletStatus());
        wrapper.set(StrUtil.isNotEmpty(walletInfoEntity.getRemark()), WalletInfoEntity::getRemark, walletInfoEntity.getRemark());

        wrapper.eq(WalletInfoEntity::getEcnyCustNo, walletInfoEntity.getEcnyCustNo());
        wrapper.eq(WalletInfoEntity::getWalletId, walletInfoEntity.getWalletId());
        try {
            return walletInfoMapper.update(wrapper);
        } catch (Exception e) {
            log.error("Failed to execute to update walletInfo", e);
            throw new BizException(EcnyErrorCode.DB_EXP);
        }
    }
}
