package com.fib.upp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.upp.entity.UserEntity;
import com.fib.upp.mapper.UserMapper;
import com.fib.upp.service.IUserService;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

}
