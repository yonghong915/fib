/**
 * 北京长信通信息技术有限公司
 * 2008-11-24 下午08:24:30
 */
package com.fib.gateway.channel.config.route;

import java.util.Map;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.util.MapUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

/**
 * 路由规则
 * 
 * @author 刘恭亮
 * 
 */
public class RouteRule {
	/**
	 * 路由规则类型：静态路由
	 */
	public static final int TYP_STATIC = 0x88;
	public static final String TYP_STATIC_TXT = "STATIC";

	/**
	 * 路由规则类型：动态路由
	 */
	public static final int TYP_DYNAMIC = 0x89;
	public static final String TYP_DYNAMIC_TXT = "DYNAMIC";

	public static int getTypeByText(String typeText) {
		if (TYP_STATIC_TXT.equalsIgnoreCase(typeText)) {
			return TYP_STATIC;
		} else if (TYP_DYNAMIC_TXT.equalsIgnoreCase(typeText)) {
			return TYP_DYNAMIC;
		} else {
			// throw new RuntimeException("Unsupport type :" + typeText);
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { typeText }));
		}
	}

	public static String getTypeText(int type) {
		switch (type) {
		case TYP_STATIC:
			return TYP_STATIC_TXT;
		case TYP_DYNAMIC:
			return TYP_DYNAMIC_TXT;
		default:
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { type + "" }));
		}
	}

	/**
	 * ID
	 */
	private String id;

	/**
	 * 路由规则类型
	 */
	private int type = TYP_STATIC;

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
	private Map determinationTable;

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
	 * @param messageType
	 *            报文类型(报文识别器识别的结果)
	 * @param message
	 *            报文(原始报文)
	 * @param messageBean
	 *            报文MB(原始报文解析后的MB)
	 * @return
	 */
	public Determination determine(String messageType, byte[] message,
			Object messageObject) {
		if (type != TYP_DYNAMIC) {
			// throw new RuntimeException("RouteRule[" + id + "]'s type is not "
			// + TYP_DYNAMIC_TXT);
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"RouteRule.determine.type.notDynamic",
							new String[] { id, TYP_DYNAMIC_TXT }));
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

		public Object get(String path) {
			if (obj instanceof MessageBean) {
				return ClassUtil.getBeanValueByPath(obj, path);
			} else {
				return MapUtil.getValue((Map) obj, path);
			}
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the destinationChannelSymbol
	 */
	public String getDestinationChannelSymbol() {
		return destinationChannelSymbol;
	}

	/**
	 * @param destinationChannelSymbol
	 *            the destinationChannelSymbol to set
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
	 * @param expression
	 *            the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @return the determinationTable
	 */
	public Map getDeterminationTable() {
		return determinationTable;
	}

	/**
	 * @param determinationTable
	 *            the determinationTable to set
	 */
	public void setDeterminationTable(Map determinationTable) {
		this.determinationTable = determinationTable;
	}

	/**
	 * @return the defaultDetermination
	 */
	public Determination getDefaultDetermination() {
		return defaultDetermination;
	}

	/**
	 * @param defaultDetermination
	 *            the defaultDetermination to set
	 */
	public void setDefaultDetermination(Determination defaultDetermination) {
		this.defaultDetermination = defaultDetermination;
	}

}
