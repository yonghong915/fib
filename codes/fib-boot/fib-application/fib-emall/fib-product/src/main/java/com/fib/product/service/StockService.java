package com.fib.product.service;

public interface StockService {
	boolean deductStock(Long productId, Integer count);
}
