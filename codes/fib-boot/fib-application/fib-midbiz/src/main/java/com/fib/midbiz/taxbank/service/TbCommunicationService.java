package com.fib.midbiz.taxbank.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.midbiz.taxbank.entity.TbCommunicationEntity;

public interface TbCommunicationService extends IService<TbCommunicationEntity> {

	List<TbCommunicationEntity> queryTbCommunicationList();
}
