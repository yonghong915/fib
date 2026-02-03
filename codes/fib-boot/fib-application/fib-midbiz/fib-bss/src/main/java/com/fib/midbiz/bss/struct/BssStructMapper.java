package com.fib.midbiz.bss.struct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.fib.midbiz.bss.entity.BssEntity;
import com.fib.midbiz.common.dto.BssDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BssStructMapper {
	BssStructMapper INSTANCE = Mappers.getMapper(BssStructMapper.class);

	@Mapping(source = "id", target = "id")
	BssDTO entityToDto(BssEntity bssEntity);

	@Mapping(source = "id", target = "id")
	BssEntity dtoToEntity(BssDTO bssDto);
}
