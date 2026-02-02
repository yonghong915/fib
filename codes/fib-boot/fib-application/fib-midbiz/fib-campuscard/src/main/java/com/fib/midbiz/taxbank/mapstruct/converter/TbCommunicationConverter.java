package com.fib.midbiz.taxbank.mapstruct.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fib.midbiz.taxbank.entity.TbCommunicationEntity;
import com.fib.midbiz.taxbank.vo.TbCommunicationVO;

@Mapper
public interface TbCommunicationConverter {
	TbCommunicationConverter INSTANCE = Mappers.getMapper(TbCommunicationConverter.class);

	@Mapping(source = "id", target = "id")
	TbCommunicationVO TbCommunicationEntity2VO(TbCommunicationEntity entity);

	TbCommunicationEntity TbCommunicationVO2Entity(TbCommunicationVO vo);
}
