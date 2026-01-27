package com.fib.mall.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fib.core.web.ResultRsp;
import com.fib.mall.dto.OrderDto;
import com.fib.mall.dto.PayDto;
import com.fib.mall.dto.ProductDto;
import com.fib.mall.entity.MallEntity;
import com.fib.mall.mapper.MallMapper;
import com.fib.mall.service.IMallService;
import com.fib.mall.service.fegin.OrderFeignService;
import com.fib.mall.service.fegin.PayFeignService;
import com.fib.mall.service.fegin.ProductFeignService;

import cn.hutool.core.util.IdUtil;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;

@Service("mallService")
public class MallServiceImpl extends ServiceImpl<MallMapper, MallEntity> implements IMallService {
	private static final Logger logger = LoggerFactory.getLogger(MallServiceImpl.class);

	@Resource
	private MallMapper mallMapper;

	@Resource
	private OrderFeignService orderFeignService;

	@Resource
	private ProductFeignService productFeignService;

	@Resource
	private PayFeignService payFeignService;

	@Override
	@GlobalTransactional(rollbackFor = Exception.class, timeoutMills = 60000)
	public boolean create(MallEntity mallEntity) {
		logger.info("开始创建订单，全局事务ID：{}", RootContext.getXID());
		Assert.notNull(mallEntity, "The mallEntity must not be null");

		try {
			/* 1.调用订单服务-创建订单 */
			OrderDto orderDto = new OrderDto();
			orderDto.setId(1l);
			orderDto.setOrderNo("202601270001");
			orderDto.setProductId(23l);
			ResultRsp<?> orderRsp = orderFeignService.createOrder(orderDto);
			if (!orderRsp.isSuccess()) {
				throw new RuntimeException("Failed to invoke createOrder of OrderService");
			}

			/* 2.调用产品服务-扣减商品 */
			ProductDto productDto = new ProductDto();
			productDto.setProductId(mallEntity.getProductId());
			productDto.setStockCount(mallEntity.getStockCount());
			ResultRsp<?> productRsp = productFeignService.deductProduct(productDto);
			if (!productRsp.isSuccess()) {
				throw new RuntimeException("Failed to invoke deductProduct of ProductService");
			}

			/* 3.调用支付服务-扣费 */
			PayDto payDto = new PayDto();
			ResultRsp<?> payRsp = payFeignService.pay(payDto);
			if (!payRsp.isSuccess()) {
				throw new RuntimeException("Failed to invoke pay of PayService");
			}

			/* 4.本地存数据库 */

//			order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
//			order.setProductId(productId);
//			order.setCount(count);
//			order.setUserId(userId);
//			order.setStatus(1); // 订单状态：成功
			mallEntity.setId(IdUtil.getSnowflakeNextId());
			mallEntity.setStatus("1");
			int rows = mallMapper.createMall(mallEntity);
			if (rows <= 0) {
				throw new RuntimeException("商城订单失败");
			}
			logger.info("商城订单成功，订单编号：{}", mallEntity.getId());
			return true;
		} catch (Exception e) {
			logger.error("订单创建失败，触发全局回滚", e);
			throw new RuntimeException("订单创建失败", e); // 抛出异常，TM通知TC回滚
		}
	}
}