package com.fib.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fib.mall.entity.MallEntity;

public interface IMallService extends IService<MallEntity> {

	boolean create(MallEntity mallEntity);
}
