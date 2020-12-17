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

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@Service("userServiceImpl")
@CacheConfig(cacheNames = "user/*")
public class UserServiceImpl implements IUserService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserMapper userMapper;

	@Cacheable(value = "aaa")
	public UserEntity getUser(String userCode) {
		logger.info("getUser--->userCode={}", userCode);
		UserEntity s = userMapper.selectById(userCode);
		userMapper.insert(s);
		logger.info("ssss={}", s.getUserCode());
		UserEntity user = new UserEntity();
		user.setUserCode(userCode);
		Snowflake sf = IdUtil.getSnowflake(1, 1);
		long pkId = sf.nextId();
		user.setPkId(pkId);
		return user;
	}
}
