package com.fib.ecny.common.bus.converter.wallet;

import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.entity.WalletInfoEntity;
import com.fib.ecny.common.bus.base.config.CustomMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = CustomMapperConfig.class, uses = WalletInfoMapperConverter.class)
public interface WalletInfoMapperConverter {
    WalletInfoMapperConverter INSTANCE = Mappers.getMapper(WalletInfoMapperConverter.class);

    WalletInfoDto toDto(WalletInfoEntity walletInfoEntity);

    List<WalletInfoDto> toDtoList(List<WalletInfoDto> walletInfoDto);

    WalletInfoEntity fromDto(WalletInfoDto walletInfoDto);
}