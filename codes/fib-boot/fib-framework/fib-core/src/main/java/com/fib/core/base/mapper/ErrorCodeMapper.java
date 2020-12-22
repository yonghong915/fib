package com.fib.core.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.core.base.entity.ErrorCodeEntity;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-17
 */
public interface ErrorCodeMapper extends BaseMapper<ErrorCodeEntity> {
	@Select("selectList")
	List<ErrorCodeEntity> selectList(ErrorCodeEntity entity);

	ErrorCodeEntity findByKey(ErrorCodeEntity entity);
}
