package com.fib.ecny.common.bus.converter.wallet;

import com.fib.ecny.common.bus.dto.wallet.WalletInfoDto;
import com.fib.ecny.common.bus.vo.wallet.WalletInfoVo;
import com.fib.ecny.entity.WalletInfoEntity;
import com.fib.ecny.common.bus.base.config.CustomMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(config = CustomMapperConfig.class, uses = WalletInfoConverter.class)
public interface WalletInfoConverter {
    WalletInfoConverter INSTANCE = Mappers.getMapper(WalletInfoConverter.class);

    WalletInfoDto toDto(WalletInfoEntity entity);
    List<WalletInfoDto> toDtoList(List<WalletInfoDto> dto);
    WalletInfoEntity fromDto(WalletInfoDto dto);

    WalletInfoDto fromVo(WalletInfoVo vo);
    WalletInfoVo toVo(WalletInfoDto dto);
}