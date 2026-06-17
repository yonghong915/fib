package com.fib.common.bus.base.comp.converter;

import com.fib.common.bus.base.comp.dto.WalletInfoDto;
import com.fib.entity.WalletInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = CustomMapperConfig.class, uses = WalletInfoStructMapperConverter.class)
public interface WalletInfoStructMapperConverter {
    WalletInfoStructMapperConverter INSTANCE = Mappers.getMapper(WalletInfoStructMapperConverter.class);

    WalletInfoDto toDto(WalletInfoEntity walletInfoEntity);

    List<WalletInfoDto> toDtoList(List<WalletInfoDto> walletInfoDto);

    WalletInfoEntity fromDto(WalletInfoDto walletInfoDto);
}