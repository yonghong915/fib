package com.fib.midbiz.taxbank.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.midbiz.taxbank.entity.BpAgreementEntity;
import com.fib.midbiz.taxbank.mapper.BpAgreementMapper;
import com.fib.midbiz.taxbank.service.BpAgreementService;

@Service("bpAgreementService")
public class BpAgreementServiceImpl extends ServiceImpl<BpAgreementMapper, BpAgreementEntity>
		implements BpAgreementService {

	@Override
	public List<BpAgreementEntity> queryBpAgreementList() {
//		LambdaQueryWrapper<CardPuncher> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(CardPuncher::getValid, ValidEnum.VALID.getCode());
//        queryWrapper.eq(CardPuncher::getId, id);
//        return this.getOne(queryWrapper);

		return list();
	}

}
