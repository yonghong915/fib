package com.fib.product.service;

import com.fib.product.entity.ProductEntity;

public interface IProductService {
	boolean deductStock(ProductEntity productEntity);
}
