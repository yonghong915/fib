package com.fib.ecny.wallet.converter;

import com.fib.ecny.base.dto.wallet.TqaWalletTranDetailDto;
import com.fib.ecny.entity.wallet.TqaWalletTranDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TqaWalletTranDetailConverter {
    TqaWalletTranDetailConverter INSTANCE = Mappers.getMapper(TqaWalletTranDetailConverter.class);
    TqaWalletTranDetailDto toDto(TqaWalletTranDetailEntity entity);
    List<TqaWalletTranDetailDto> toDtoList(List<TqaWalletTranDetailDto> dto);
    TqaWalletTranDetailEntity fromDto(TqaWalletTranDetailDto dto);
}
