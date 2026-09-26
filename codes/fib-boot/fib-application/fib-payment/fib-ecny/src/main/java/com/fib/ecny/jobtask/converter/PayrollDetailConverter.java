package com.fib.ecny.jobtask.converter;

import com.fib.ecny.jobtask.PayrollDetailDto;
import com.fib.ecny.jobtask.entity.PayrollDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface PayrollDetailConverter {
    PayrollDetailConverter INSTANCE = Mappers.getMapper(PayrollDetailConverter.class);

    PayrollDetailDto toDto(PayrollDetailEntity entity);

    List<PayrollDetailDto> toDtoList(List<PayrollDetailEntity> dto);

    PayrollDetailEntity fromDto(PayrollDetailDto dto);
}
