package com.fib.core.base.service;

import java.util.List;

import com.fib.core.base.entity.ErrorCodeEntity;

public interface IErrorCodeService {
	List<ErrorCodeEntity> selectList(ErrorCodeEntity entity);
}
