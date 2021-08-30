package com.fib.msgconverter.commgateway.dao.moduleparameterrelation.dao;

import java.math.BigDecimal;

public class ModuleParameterRelation{
	public ModuleParameterRelation() {

	}

	//组件ID
	private String moduleId;

	//参数ID
	private String parameterId;

	public void setModuleId(String newModuleId) {
		this.moduleId = newModuleId;
	}

	public void setParameterId(String newParameterId) {
		this.parameterId = newParameterId;
	}

	public String getModuleId() {
		return this.moduleId;
	}

	public String getParameterId() {
		return this.parameterId;
	}

}