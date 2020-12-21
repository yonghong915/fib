package com.fib.uias.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000, multiplier = 1, maxDelay = 10000))
	@Override
	public void retryTransferAccounts(int fromAccountId, int toAccountId, float money) throws Exception {
		logger.info("----retryTransferAccounts--");

		throw new Exception();
	}

	@Recover
	public void recover(Exception e) {
		System.out.println("回调方法执行！！！");
	}
}
