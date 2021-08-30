package com.fib.msgconverter.commgateway.dao.routerule.dao;

import java.math.BigDecimal;

public class RouteRule{
	public RouteRule() {

	}

	//id
	private String id;

	//路由规则类型
	private String routeRuleType;

	//目的通道ID
	private String destChannelId;

	//动态路由表达式
	private String expression;

	//动态路由集合ID
	private String determinationId;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setRouteRuleType(String newRouteRuleType) {
		this.routeRuleType = newRouteRuleType;
	}

	public void setDestChannelId(String newDestChannelId) {
		this.destChannelId = newDestChannelId;
	}

	public void setExpression(String newExpression) {
		this.expression = newExpression;
	}

	public void setDeterminationId(String newDeterminationId) {
		this.determinationId = newDeterminationId;
	}

	public String getId() {
		return this.id;
	}

	public String getRouteRuleType() {
		return this.routeRuleType;
	}

	public String getDestChannelId() {
		return this.destChannelId;
	}

	public String getExpression() {
		return this.expression;
	}

	public String getDeterminationId() {
		return this.determinationId;
	}

}