package com.fib.product.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.product.entity.ProductEntity;

public interface ProductMapper extends BaseMapper<ProductEntity> {
	// 扣减库存（加锁防止超卖）
	@Update("UPDATE t_stock SET stock_count = stock_count - #{count} "
			+ "WHERE product_id = #{productId} AND stock_count >= #{count}")
	int deductStock(@Param("productId") Long productId, @Param("count") Integer count);
}
