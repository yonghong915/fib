package com.fib.midbiz.taxbank.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.midbiz.taxbank.entity.BpAgreementEntity;

public interface BpAgreementService extends IService<BpAgreementEntity> {
	public List<BpAgreementEntity> queryBpAgreementList();
}
