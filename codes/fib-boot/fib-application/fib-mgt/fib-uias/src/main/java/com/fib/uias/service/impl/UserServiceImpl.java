package com.fib.uias.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fib.commons.exception.BusinessException;
import com.fib.core.util.StatusCode;
import com.fib.uias.entity.UserEntity;
import com.fib.uias.mapper.UserMapper;
import com.fib.uias.service.IUserService;

@Service("userServiceImpl")
@CacheConfig(cacheNames = "user/*")
public class UserServiceImpl implements IUserService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserMapper userMapper;

	public UserEntity getUser(String userCode) {
		logger.info("getUser--->userCode={}", userCode);
		try {
			return userMapper.selectById(userCode);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION);
		}
	}

	public int addUser(UserEntity userEntity) {
		return userMapper.insert(userEntity);
	}

	@Transactional
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000, multiplier = 1, maxDelay = 10000))
	@Override
	public void retryTransferAccounts(int fromAccountId, int toAccountId, float money) throws Exception {
		logger.info("----retryTransferAccounts--");

	}

	@Recover
	public void recover(Exception e) {
		logger.info("回调方法执行！！！");
	}
}
