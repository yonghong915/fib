package com.fib.ecny.wallet.converter;

import com.fib.ecny.wallet.api.dto.WalletInfoDto;
import com.fib.ecny.wallet.entity.WalletInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = CustomMapperConfig.class, uses = WalletInfoConverter.class)
public interface WalletInfoConverter {
    WalletInfoConverter INSTANCE = Mappers.getMapper(WalletInfoConverter.class);

    WalletInfoDto toDto(WalletInfoEntity entity);
    List<WalletInfoDto> toDtoList(List<WalletInfoDto> dto);
    WalletInfoEntity fromDto(WalletInfoDto dto);

//    WalletInfoDto fromVo(WalletInfoVo vo);
//    WalletInfoVo toVo(WalletInfoDto dto);
}