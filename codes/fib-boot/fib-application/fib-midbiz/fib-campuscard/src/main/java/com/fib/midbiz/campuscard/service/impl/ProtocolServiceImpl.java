package com.fib.midbiz.campuscard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.midbiz.campuscard.entity.ProtocolEntity;
import com.fib.midbiz.campuscard.mapper.ProtocolMapper;
import com.fib.midbiz.campuscard.service.IProtocolService;
import com.fib.midbiz.campuscard.struct.ProtocolStructMapper;
import com.fib.midbiz.common.dto.campuscard.ProtocolDTO;

@Service("protocolService")
public class ProtocolServiceImpl extends ServiceImpl<ProtocolMapper, ProtocolEntity> implements IProtocolService {

	@Autowired
	private ProtocolMapper protocolMapper;

	@Autowired
	private ProtocolStructMapper protocolStructMapper;

	@Override
	public int signProtocol(ProtocolDTO protocoldto) {
		return protocolMapper.insert(protocolStructMapper.dtoToEntity(protocoldto));
	}
}
