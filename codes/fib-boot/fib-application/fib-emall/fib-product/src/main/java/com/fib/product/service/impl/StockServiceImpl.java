package com.fib.product.service.impl;

import org.springframework.stereotype.Service;

import com.fib.product.mapper.StockMapper;
import com.fib.product.service.StockService;

import jakarta.annotation.Resource;

@Service
public class StockServiceImpl implements StockService {

	@Resource
	private StockMapper stockMapper;

	@Override
	public boolean deductStock(Long productId, Integer count) {
		int rows = stockMapper.deductStock(productId, count);
		return rows > 0; // 扣减成功返回true，失败返回false
	}
}