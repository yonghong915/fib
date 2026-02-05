package com.fib.midbiz.campuscard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.midbiz.campuscard.entity.ProtocolEntity;
import com.fib.midbiz.common.dto.campuscard.ProtocolDTO;

public interface IProtocolService extends IService<ProtocolEntity> {
	int signProtocol(ProtocolDTO protocoldto);
}
