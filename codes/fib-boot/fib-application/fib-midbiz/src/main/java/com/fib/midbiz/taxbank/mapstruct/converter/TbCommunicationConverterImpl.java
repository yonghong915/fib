package com.fib.midbiz.taxbank.mapstruct.converter;

import javax.annotation.processing.Generated;

import com.fib.midbiz.taxbank.entity.TbCommunicationEntity;
import com.fib.midbiz.taxbank.vo.TbCommunicationVO;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class TbCommunicationConverterImpl implements TbCommunicationConverter {

	@Override
	public TbCommunicationVO TbCommunicationEntity2VO(TbCommunicationEntity entity) {
		if (null == entity) {
			return null;
		}
		TbCommunicationVO vo = new TbCommunicationVO();
		vo.setId(entity.getId());
		return vo;
	}

	@Override
	public TbCommunicationEntity TbCommunicationVO2Entity(TbCommunicationVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

}
