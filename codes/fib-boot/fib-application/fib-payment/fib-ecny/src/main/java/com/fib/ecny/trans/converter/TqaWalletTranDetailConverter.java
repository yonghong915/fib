package com.fib.ecny.trans.converter;

import com.fib.ecny.base.api.trans.dto.TqaWalletTranDetailDto;
import com.fib.ecny.trans.entity.TqaWalletTranDetailEntity;
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
