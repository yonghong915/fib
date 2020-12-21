package com.fib.core.base.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.core.base.entity.ErrorCodeEntity;
import com.fib.core.base.mapper.ErrorCodeMapper;
import com.fib.core.base.service.IErrorCodeService;
import com.fib.core.exception.BusinessException;
import com.fib.core.util.BloomFilterHelper;
import com.fib.core.util.RedisUtil;
import com.fib.core.util.StatusCode;
import com.google.common.hash.Funnel;

import cn.hutool.core.collection.CollUtil;

@Service("errorCodeService")
@CacheConfig(cacheNames = "errorCode-Cache")
public class ErrorCodeServiceImpl extends ServiceImpl<ErrorCodeMapper, ErrorCodeEntity> implements IErrorCodeService {

	@Autowired
	private ErrorCodeMapper errorCodeMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private RedisService redisService;

	@Override
	public List<ErrorCodeEntity> selectList(ErrorCodeEntity entity) {
		BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into
				.putString(from, StandardCharsets.UTF_8).putString(from, StandardCharsets.UTF_8), 1500000, 0.00001);
//		boolean answer = redisService.includeByBloomFilter(myBloomFilterHelper,
//				entity.getLanguage() + "~" + entity.getErrorCode(), "");
		Object obj = redisUtil.get(entity.getLanguage() + "~" + entity.getErrorCode());
		try {
			if (Objects.isNull(obj)) {
				return errorCodeMapper.selectList(entity);
			}
		} catch (Exception e) {
			throw new BusinessException(StatusCode.DB_EXCEPTION);
		}
		
		return CollUtil.newArrayList((ErrorCodeEntity)obj);
	}

}
