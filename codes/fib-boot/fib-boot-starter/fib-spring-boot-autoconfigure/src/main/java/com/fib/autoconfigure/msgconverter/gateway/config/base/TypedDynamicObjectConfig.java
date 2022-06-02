package com.fib.autoconfigure.msgconverter.gateway.config.base;

import org.apache.dubbo.rpc.model.ApplicationModel;

import com.fib.commons.exception.CommonException;

/**
 * 带类型的动态对象配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-23 14:51:14
 */
public abstract class TypedDynamicObjectConfig extends DynamicObjectConfig {
	public static final String USER_DEFINED = "USER_DEFINED";

	protected String type;

	protected Class<?> objInterface;

	protected abstract void setObjInterface();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		setObjInterface();
		if (type != null && !USER_DEFINED.equalsIgnoreCase(type)) {
			if (null == objInterface) {
				return;
			}
			Object obj = ApplicationModel.defaultModel().getDefaultModule().getExtension(objInterface, type);
			if (null == obj) {
				throw new CommonException("type not support");
			}
			setClassName(obj.getClass().getName());
		}
	}
}
