package com.fib.order.mapper;

import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.order.entity.OrderEntity;

public interface OrderMapper extends BaseMapper<OrderEntity> {
	@Insert("INSERT INTO t_order(order_no, product_id, count, user_id, status) "
			+ "VALUES(#{orderNo}, #{productId}, #{count}, #{userId}, #{status})")
	int createOrder(OrderEntity order);
}
