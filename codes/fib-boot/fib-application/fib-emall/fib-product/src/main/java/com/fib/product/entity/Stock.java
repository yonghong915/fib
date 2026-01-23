package com.fib.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_stock")
public class Stock {
	private Long id;
	private Long productId;
	private Integer stockCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
}