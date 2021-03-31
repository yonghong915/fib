package com.fib.gateway.message.bean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.metadata.Message;

/**
 * 公共MessageBean
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public class CommonMessageBean extends MessageBean {
	private Map values = new LinkedHashMap(64);

	public Map getValues() {
		return this.values;
	}

	public void setValues(Map var1) {
		this.values = var1;
	}

	@Override
	public void validate() {
		this.validate(this.getMetadata());
	}

	private void validate(Message message) {
		if (Objects.isNull(message)) {
			throw new CommonException("message not exists.");
		}
	}

	@Override
	public String toString(boolean isWrap, boolean isTable) {
		return null;
	}

}
