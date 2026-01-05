package com.fib.uqcp.statemachine;

import java.util.EnumSet;

import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import jakarta.annotation.Resource;

//@Configuration
//@EnableStateMachine
public class OrderStatusMachineConfig extends StateMachineConfigurerAdapter<OrderStatusEnum, OrderStatusChangeEventEnum> {
	@Resource
	private OrderStatusListener orderStateListener;

	@Override
	public void configure(StateMachineStateConfigurer<OrderStatusEnum, OrderStatusChangeEventEnum> states) throws Exception {
		states.withStates()
				// 设置初始化状态
				.initial(OrderStatusEnum.WAIT_PAYMENT)
				// 设置用于条件判断的状态
				.choice(OrderStatusEnum.FINISH)
				// 绑定全部状态
				.states(EnumSet.allOf(OrderStatusEnum.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderStatusEnum, OrderStatusChangeEventEnum> transitions) throws Exception {
		// 1、withExternal 是当source和target不同时的写法
		// 2、withInternal 当source和target相同时的串联写法，比如付款失败时，付款前及付款后都是待付款状态
		// 3、withChoice 当执行一个动作，可能导致多种结果时，可以选择使用choice+guard来跳转
//		transitions
//				// 通过PAYED 实现由 WAIT_PAYMENT => WAIT_DELIVER 状态转移
//				.withExternal().source(OrderStatusEnum.WAIT_PAYMENT).target(OrderStatusEnum.WAIT_DELIVER).event(OrderStatusChangeEventEnum.PAYED)
//				.guard(payedGuard).action(payedAction).and()
//				// 通过DELIVERY 实现由 WAIT_DELIVER => WAIT_RECEIVE 状态转移
//				.withExternal().source(OrderStatusEnum.WAIT_DELIVER).target(OrderStatusEnum.WAIT_RECEIVE).event(OrderStatusChangeEventEnum.DELIVERY)
//				.guard(deliveryGuard).action(deliveryAction).and()
//				// 通过RECEIVED 实现由 WAIT_RECEIVE => FINISH 状态转移
//				.withExternal().source(OrderStatusEnum.WAIT_RECEIVE).target(OrderStatusEnum.FINISH).event(OrderStatusChangeEventEnum.RECEIVED)
//				.guard(receivedGuard).action(receivedAction).and()
//				// Choice的状态选择，
//				// commentGuard的结果为true则执行first
//				// commentGuard的结果为true则执行then
//				.withChoice().source(OrderStatusEnum.FINISH).first(OrderStatusEnum.COMMENTED, commentGuard, commentedAction)
//				.then(OrderStatusEnum.UNCOMMENTED, commentGuard, uncommentedAction).last(OrderStatusEnum.WAIT_COMMENT);
	}
	
	@Override
	public void configure(StateMachineConfigurationConfigurer<OrderStatusEnum, OrderStatusChangeEventEnum> config) throws Exception {
		// 状态转移监听事件
		config.withConfiguration().listener(orderStateListener);
	}
}
