package com.fib.midbiz.taxbank.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.midbiz.taxbank.entity.TbCommunicationEntity;
import com.fib.midbiz.taxbank.mapper.TbCommunicationMapper;
import com.fib.midbiz.taxbank.service.TbCommunicationService;

@Service
public class TbCommunicationServiceImpl extends ServiceImpl<TbCommunicationMapper, TbCommunicationEntity> implements TbCommunicationService {

	@Override
	public List<TbCommunicationEntity> queryTbCommunicationList() {
//		LambdaQueryWrapper<CardPuncher> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(CardPuncher::getValid, ValidEnum.VALID.getCode());
//        queryWrapper.eq(CardPuncher::getId, id);
//        return this.getOne(queryWrapper);
		
		return list();
	}

}
