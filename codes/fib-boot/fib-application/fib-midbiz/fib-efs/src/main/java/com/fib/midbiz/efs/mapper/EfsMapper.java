package com.fib.midbiz.efs.mapper;

import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.midbiz.efs.entity.EfsEntity;

public interface EfsMapper extends BaseMapper<EfsEntity> {

	@Insert("INSERT INTO t_efs(pk_id) VALUES(#{id})")
	int createEfs(EfsEntity efsEntity);
}
