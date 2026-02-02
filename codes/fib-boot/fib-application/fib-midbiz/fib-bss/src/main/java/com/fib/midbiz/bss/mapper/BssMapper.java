package com.fib.midbiz.bss.mapper;

import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.midbiz.bss.entity.BssEntity;

public interface BssMapper extends BaseMapper<BssEntity> {

	@Insert("INSERT INTO t_bss(pk_id) VALUES(#{id})")
	int createBss(BssEntity efsEntity);
}
