package com.fib.midbiz.campuscard.struct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.fib.midbiz.campuscard.entity.ProtocolEntity;
import com.fib.midbiz.common.dto.campuscard.ProtocolDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProtocolStructMapper {
	ProtocolStructMapper INSTANCE = Mappers.getMapper(ProtocolStructMapper.class);

	ProtocolDTO entityToDto(ProtocolEntity entity);

	ProtocolEntity dtoToEntity(ProtocolDTO dto);
}
