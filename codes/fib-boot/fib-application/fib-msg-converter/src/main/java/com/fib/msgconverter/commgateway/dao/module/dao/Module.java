package com.fib.msgconverter.commgateway.dao.module.dao;

import java.math.BigDecimal;

public class Module{
	public Module() {

	}

	//id
	private String id;

	//组件类名
	private String moduleClass;

	//所属网关ID
	private String gatewayId;

	//是否加载
	private String deploy;

	public void setId(String newId) {
		this.id = newId;
	}

	public void setModuleClass(String newModuleClass) {
		this.moduleClass = newModuleClass;
	}

	public void setGatewayId(String newGatewayId) {
		this.gatewayId = newGatewayId;
	}

	public void setDeploy(String newDeploy) {
		this.deploy = newDeploy;
	}

	public String getId() {
		return this.id;
	}

	public String getModuleClass() {
		return this.moduleClass;
	}

	public String getGatewayId() {
		return this.gatewayId;
	}

	public String getDeploy() {
		return this.deploy;
	}

}