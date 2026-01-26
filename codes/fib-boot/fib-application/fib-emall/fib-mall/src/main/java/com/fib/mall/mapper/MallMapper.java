package com.fib.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.mall.entity.MallEntity;

public interface MallMapper extends BaseMapper<MallEntity> {

	int createMall(MallEntity mallEntity);
}
