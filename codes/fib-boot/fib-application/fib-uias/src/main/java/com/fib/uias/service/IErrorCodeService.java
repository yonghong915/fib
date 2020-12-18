package com.fib.uias.service;

import java.util.List;

import com.fib.uias.entity.ErrorCodeEntity;

public interface IErrorCodeService {
	List<ErrorCodeEntity> selectList(ErrorCodeEntity entity);
}
