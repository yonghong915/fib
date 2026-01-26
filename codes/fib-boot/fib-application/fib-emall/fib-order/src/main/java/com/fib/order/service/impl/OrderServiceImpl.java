package com.fib.order.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fib.order.entity.OrderEntity;
import com.fib.order.mapper.OrderMapper;
import com.fib.order.service.OrderService;
import com.fib.order.service.feign.StockFeignClient;

import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Resource
	private OrderMapper orderMapper;
	@Resource
	private StockFeignClient stockFeignClient;

	/**
	 * 创建订单+扣减库存：分布式事务入口
	 * 
	 * @GlobalTransactional：Seata全局事务注解，TM发起全局事务
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createOrder(OrderEntity orderEntity) {
		logger.info("开始创建订单，全局事务ID：{}", RootContext.getXID());

		try {
//			// 1. 远程调用库存服务扣减库存
//			ResultRsp<Boolean> deductResult = stockFeignClient.deductStock(productId, count);
//			if (!deductResult.isSuccess() || deductResult.getRspObj() != null) {
//				throw new RuntimeException("库存扣减失败");
//			}

			// 2. 本地创建订单
			OrderEntity order = new OrderEntity();
			order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
			order.setProductId(1l);
			order.setCount(2);
			order.setUserId(2l);
			order.setStatus(1); // 订单状态：成功
			int rows = orderMapper.createOrder(order);
			if (rows <= 0) {
				throw new RuntimeException("订单创建失败");
			}
			logger.info("订单创建成功，订单编号：{}", order.getOrderNo());
			return true;
		} catch (Exception e) {
			logger.error("订单创建失败，触发全局回滚", e);
			throw new RuntimeException("订单创建失败", e); // 抛出异常，TM通知TC回滚
		}
	}
}