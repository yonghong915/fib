package com.fib.uias.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fib.uias.entity.UserEntity;
import com.fib.uias.mapper.UserMapper;
import com.fib.uias.service.IUserService;

@Service("userServiceImpl")
@CacheConfig(cacheNames = "user/*")
public class UserServiceImpl implements IUserService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserMapper userMapper;

	@Cacheable(value = "aaa")
	public UserEntity getUser(String userCode) {
		logger.info("getUser--->userCode={}", userCode);
		return userMapper.selectById(userCode);
	}

	public int addUser(UserEntity userEntity) {
		return userMapper.insert(userEntity);
	}
}
