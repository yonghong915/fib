package com.fib.ecny.wallet;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fib.ecny.base.dto.wallet.WalletInfoDto;
import com.fib.ecny.base.enums.EcnyErrorCode;
import com.fib.ecny.base.exception.BizException;
import com.fib.ecny.entity.wallet.TqaWalletTranDetailEntity;
import com.fib.ecny.entity.wallet.WalletInfoEntity;
import com.fib.ecny.mapper.TqaWalletTranDetailMapper;
import com.fib.ecny.mapper.WalletInfoMapper;
import com.fib.ecny.wallet.converter.WalletInfoConverter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 钱包信息组件
 */
@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class WalletInfoComp {
    private final WalletInfoMapper walletInfoMapper;
    private final TqaWalletTranDetailMapper tqaWalletTranDetailMapper;

    record TransStatus(String chnlNo, String chnlSeqNo) {
        public TransStatus {
            if (chnlNo == null || chnlNo.isBlank()) {
                throw new IllegalArgumentException("渠道号不能为空");
            }
        }

        public boolean isValid() {
            return chnlSeqNo != null && !chnlSeqNo.isBlank();
        }

        public static TransStatus of(String chnlNo, String chnlSeqNo) {
            return new TransStatus(chnlNo, chnlSeqNo);
        }

        record Person(String name, int age) {
        }
    }

    record Pair(String hash, Long timestamp) {
    }

    record TqaExtFields(String payerWalletId, String payeeWalletId) {
    }

    public void update(TransStatus transStatus, Supplier<TqaWalletTranDetailEntity> supplier) {
        LambdaUpdateWrapper<TqaWalletTranDetailEntity> updateWrapper = Wrappers.lambdaUpdate(TqaWalletTranDetailEntity.class);
        TqaWalletTranDetailEntity updateEntity = supplier.get();
        if (Objects.isNull(updateEntity)) {
            log.warn("更新TqaWalletTranDetail，supplier返回实体为空");
            return;
        }
        updateWrapper.eq(TqaWalletTranDetailEntity::getChnlSerialNo, transStatus.chnlSeqNo())
                .eq(TqaWalletTranDetailEntity::getGlSerialNo, transStatus.chnlNo());
        int affectRows = tqaWalletTranDetailMapper.update(updateEntity, updateWrapper);
    }

    public void getWalletID() {
        TransStatus transStatus = new TransStatus("1222", "344");
        update(transStatus, () -> {
            TqaWalletTranDetailEntity entity = TqaWalletTranDetailEntity.builder()
                    .glSerialNo("122333")
                    .chnlSerialNo("12333")
                    .build();
            return entity;
        });
    }

    public Optional<WalletInfoDto> getWalletInfo(WalletInfoDto walletInfoDto) {
        if (Objects.isNull(walletInfoDto) || Objects.isNull(walletInfoDto.getEcnyCustNo()) || Objects.isNull(walletInfoDto.getWalletId())) {
            return Optional.empty();
        }

        LambdaQueryWrapper<WalletInfoEntity> wrapper = Wrappers.lambdaQuery(WalletInfoEntity.class);
        WalletInfoEntity conditionEntity = WalletInfoConverter.INSTANCE.fromDto(walletInfoDto);
        wrapper.select(WalletInfoEntity::getEcnyCustNo, WalletInfoEntity::getWalletId)
                .eq(WalletInfoEntity::getWalletId, conditionEntity.getWalletId())
                .eq(WalletInfoEntity::getEcnyCustNo, conditionEntity.getEcnyCustNo());
        try {
            return Optional.ofNullable(WalletInfoConverter.INSTANCE.toDto(walletInfoMapper.selectOne(wrapper)));
        } catch (Exception e) {
            log.error("查询钱包信息异常,ecnyCustNo=[{}],walletId=[{}]", conditionEntity.getEcnyCustNo(), conditionEntity.getWalletId(), e);
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
