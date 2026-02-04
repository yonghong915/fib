package com.fib.uqcp.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.uqcp.entity.UserRole;

public interface UserRoleMapper {

	List<UserRole> selectList(QueryWrapper<UserRole> queryWrapper1);

}
