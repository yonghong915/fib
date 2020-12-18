package com.fib.uias.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.uias.entity.ErrorCodeEntity;
import com.fib.uias.mapper.ErrorCodeMapper;
import com.fib.uias.service.IErrorCodeService;

@Service("errorCodeService")
public class ErrorCodeServiceImpl extends ServiceImpl<ErrorCodeMapper, ErrorCodeEntity> implements IErrorCodeService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ErrorCodeMapper errorCodeMapper;

	public List<ErrorCodeEntity> selectList(ErrorCodeEntity entity) {
		return errorCodeMapper.selectList(queryWrapper).selectList(entity);
	}

}
