package com.fib.msgconverter.commgateway.channel.net.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * Writer抽象实现类
 * 
 * @author 刘恭亮
 */
public abstract class AbstractWriter implements Writer {
	/**
	 * 参数
	 */
	protected Map parameters = null;

	/**
	 * 调试器
	 */
	protected Logger logger = null;

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	/**
	 * 过滤器链
	 */
	protected List<AbstractMessageFilter> filterList = new ArrayList<>();

	public List<AbstractMessageFilter> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<AbstractMessageFilter> filterList) {
		this.filterList = filterList;
	}

	public void write(OutputStream out, byte[] message) {
		for (int i = 0; i < filterList.size(); i++) {
			AbstractMessageFilter filter = (AbstractMessageFilter) filterList
					.get(i);
			String filterClassName = filter.getClass().getName();

			if (logger.isDebugEnabled()) {
				// logger.debug("message before filter[" + filterClassName
				// + "]:\n" + CodeUtil.Bytes2FormattedText(message));
				logger
						.debug(MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"message.beforeFilter",
										new String[] {
												filterClassName,
												CodeUtil
														.Bytes2FormattedText(message) }));
			}
			message = filter.doFilter(message);
			if (logger.isDebugEnabled()) {
				// logger.debug("message after filter[" + filterClassName +
				// "]:\n"
				// + CodeUtil.Bytes2FormattedText(message));
				logger
						.debug(MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"message.afterFilter",
										new String[] {
												filterClassName,
												CodeUtil
														.Bytes2FormattedText(message) }));
			}
		}

		try {
			out.write(message);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
