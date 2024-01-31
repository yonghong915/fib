package com.fib.uqcp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.uqcp.entity.SysUser;
import com.fib.uqcp.mapper.SysUserMapper;
import com.fib.uqcp.service.SysUserService;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}