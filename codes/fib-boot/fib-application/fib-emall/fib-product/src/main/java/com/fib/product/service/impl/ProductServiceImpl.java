package com.fib.product.service.impl;

import org.springframework.stereotype.Service;

import com.fib.product.entity.ProductEntity;
import com.fib.product.mapper.ProductMapper;
import com.fib.product.service.IProductService;

import jakarta.annotation.Resource;

@Service
public class ProductServiceImpl implements IProductService {

	@Resource
	private ProductMapper productMapper;

	@Override
	public boolean deductStock(ProductEntity productEntity) {
		int rows = productMapper.deductStock(productEntity.getProductId(), productEntity.getStockCount());
		return rows > 0; // 扣减成功返回true，失败返回false
	}
}