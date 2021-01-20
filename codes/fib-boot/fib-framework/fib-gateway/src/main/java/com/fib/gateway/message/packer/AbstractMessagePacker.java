package com.fib.gateway.message.packer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.Variable;
import com.fib.gateway.message.util.ByteBuffer;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

/**
 * 消息组包器基类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public abstract class AbstractMessagePacker {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/***/
	protected Message message = null;

	/***/
	protected MessageBean messageBean = null;

	/***/
	protected MessageBean parentBean = null;

	protected ByteBuffer buf = null;

	protected Map<String, Object> variableCache = new HashMap<>(5);

	private String defaultCharset = System.getProperty("file.encoding");

	public abstract byte[] pack();

	protected void loadVariable() {
		if (CollUtil.isNotEmpty(this.message.getVariable())) {
			Iterator<Variable> variableIter = this.message.getVariable().values().iterator();
			Variable variable = null;
			Object variableValue = null;
			while (variableIter.hasNext()) {
				variable = variableIter.next();
				this.variableCache.put(variable.getName(), variableValue);
				switch (variable.getDataType()) {
				case 3000:
				case 3001:
					variableValue = variable.getValue();
					break;

				case 3003:
				case 3007:
					variableValue = Integer.valueOf(variable.getValue());
					break;

				case 3004:
					variableValue = Byte.valueOf(variable.getValue());
					break;

				case 3005:
				case 3008:
					variableValue = Short.valueOf(variable.getValue());
					break;

				case 3002:
				case 3006:
				default:
					throw new CommonException("tableRowField.dataType.unsupport");
				}
			}
		}
	}
}
