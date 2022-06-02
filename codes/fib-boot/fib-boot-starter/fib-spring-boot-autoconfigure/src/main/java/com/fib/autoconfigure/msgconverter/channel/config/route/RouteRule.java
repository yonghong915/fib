package com.fib.autoconfigure.msgconverter.channel.config.route;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fib.autoconfigure.msgconverter.message.bean.MessageBean;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;
import com.fib.autoconfigure.msgconverter.util.BeanShellEngine;
import com.fib.commons.exception.CommonException;

public class RouteRule {
	/**
	 * ID
	 */
	private String id;

	/**
	 * 路由规则类型
	 */
	private ConstantMB.RouteRuleType type = ConstantMB.RouteRuleType.STATIC;

	/**
	 * 目的通道符号
	 */
	private String destinationChannelSymbol;

	/**
	 * 动态路由的表达式
	 */
	private String expression;

	/**
	 * 决定表。动态路由的表达式的一个值对应一个决定。
	 */
	private Map<String, Determination> determinationTable;

	/**
	 * 默认决定
	 */
	private Determination defaultDetermination;

	/**
	 * 表达式脚本执行器(BeanShell)
	 */
	private BeanShellEngine executor = null;

	/**
	 * 决定动态路由的结果
	 * 
	 * @param messageType 报文类型(报文识别器识别的结果)
	 * @param message     报文(原始报文)
	 * @param messageBean 报文MB(原始报文解析后的MB)
	 * @return
	 */
	public Determination determine(String messageType, byte[] message, Object messageObject) {
		if (type != ConstantMB.RouteRuleType.DYNAMIC) {
			throw new CommonException("RouteRule.determine.type.notDynamic");
		}

		// 1. 执行表达式
		if (null == executor) {
			executor = new BeanShellEngine();
		}
		String result = null;
		// try {
		executor.set("messageType", messageType);
		executor.set("message", message);
		// executor.set("messageObject", messageObject);
		executor.set("messageObject", new DataObject(messageObject));
		if (messageObject instanceof Map) {
			executor.set("map", messageObject);
		} else if (messageObject instanceof MessageBean) {
			executor.set("bean", messageObject);
		}
		Object ret = executor.eval(expression);
		if (ret != null) {
			if (ret instanceof String) {
				result = (String) ret;
			} else {
				result = ret.toString();
			}
		}
		// } catch (EvalError e) {
		// ExceptionUtil.throwActualException(e);
		// }

		// 2. 决定结果
		Determination d = (Determination) determinationTable.get(result);
		if (null == d) {
			d = defaultDetermination;
		}
		return d;
	}

	public class DataObject {
		Object obj;

		DataObject(Object obj) {
			this.obj = obj;
		}

		// TODO
		public Object get(String path) {
			if (obj instanceof MessageBean) {
				// return ClassUtil.getBeanValueByPath(obj, path);
			} else {
				// return MapUtil.getValue((Map) obj, path);
			}
			return path;
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public ConstantMB.RouteRuleType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ConstantMB.RouteRuleType type) {
		this.type = type;
	}

	/**
	 * @return the destinationChannelSymbol
	 */
	public String getDestinationChannelSymbol() {
		return destinationChannelSymbol;
	}

	/**
	 * @param destinationChannelSymbol the destinationChannelSymbol to set
	 */
	public void setDestinationChannelSymbol(String destinationChannelSymbol) {
		this.destinationChannelSymbol = destinationChannelSymbol;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @return the determinationTable
	 */
	public Map<String, Determination> getDeterminationTable() {
		return determinationTable;
	}

	/**
	 * @param determinationTable the determinationTable to set
	 */
	public void setDeterminationTable(Map<String, Determination> determinationTable) {
		this.determinationTable = determinationTable;
	}

	/**
	 * @return the defaultDetermination
	 */
	public Determination getDefaultDetermination() {
		return defaultDetermination;
	}

	/**
	 * @param defaultDetermination the defaultDetermination to set
	 */
	public void setDefaultDetermination(Determination defaultDetermination) {
		this.defaultDetermination = defaultDetermination;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
