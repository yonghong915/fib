package com.fib.uqcp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.uqcp.entity.ChineseMedicine;

@Mapper
public interface ChineseMedicineMapper extends BaseMapper<ChineseMedicine>{
	 int insert(ChineseMedicine s);
}
