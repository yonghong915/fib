package com.fib.midbiz.bss.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.midbiz.bss.entity.BssEntity;

public interface BssMapper extends BaseMapper<BssEntity> {

	@Insert("INSERT INTO t_bss(pk_id) VALUES(#{id})")
	int createBss(BssEntity efsEntity);

	@Select("SELECT pk_id as id FROM t_bss WHERE pk_id = #{id}")
	Optional<BssEntity> getBssById(Long userId);
}
