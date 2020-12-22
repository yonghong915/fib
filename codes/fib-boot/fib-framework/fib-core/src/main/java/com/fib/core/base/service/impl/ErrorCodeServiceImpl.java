package com.fib.core.base.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.mapper.ErrorCodeMapper;
import com.fib.core.base.service.IErrorCodeService;
import com.fib.core.config.BloomFilterConfig;
import com.fib.core.exception.BusinessException;
import com.fib.core.util.RedisUtil;
import com.fib.core.util.StatusCode;

import cn.hutool.core.collection.CollUtil;

/**
 * 错误码服务
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-22
 */
@Service("errorCodeService")
@CacheConfig(cacheNames = "errorCode-Cache")
public class ErrorCodeServiceImpl extends ServiceImpl<ErrorCodeMapper, ErrorCodeEntity> implements IErrorCodeService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ErrorCodeMapper errorCodeMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private BloomFilterConfig bloomFilterConfig;

	/**
	 * @param entity
	 * @return
	 */
	@Override
	public List<ErrorCodeEntity> selectList(ErrorCodeEntity entity) {
		String key = entity.getLanguage() + "~" + entity.getErrorCode();
		logger.info("selectList->params key:{}", key);
		try {
			return errorCodeMapper.selectList(entity);
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION);
		}
	}

	@Override
	public ErrorCodeEntity findByKey(ErrorCodeEntity entity) {
		String key = "errorCode~" + entity.getLanguage() + "~" + entity.getErrorCode();
		boolean existsFlag = bloomFilterConfig.isContains(key);
		logger.info("findByKey->errorCode bloomFilter {} is exists:{}", key, existsFlag);
		if (!existsFlag) {
			return null;
		}
		Object obj = redisUtil.get(key);
		try {
			if (Objects.isNull(obj)) {
				return errorCodeMapper.findByKey(entity);
			}
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION);
		}
		return (ErrorCodeEntity) obj;
	}
}
