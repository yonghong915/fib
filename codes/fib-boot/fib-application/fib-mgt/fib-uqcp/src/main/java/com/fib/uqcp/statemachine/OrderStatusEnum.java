package com.fib.uqcp.statemachine;

public enum OrderStatusEnum {

	WAIT_PAYMENT, // 待付款
	WAIT_DELIVER, // 待发货
	WAIT_RECEIVE, // 待收货
	FINISH, // 已收货
	WAIT_COMMENT, // 待评论
	COMMENTED, // 已评论
	UNCOMMENTED; // 未评论
}
