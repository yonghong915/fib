package com.fib.uqcp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.uqcp.entity.Role;

public interface RoleMapper {

	Role selectOne(QueryWrapper<Role> queryWrapper2);

}
