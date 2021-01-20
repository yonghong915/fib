
package com.fib.gateway.channel.nio.config;

import java.util.ArrayList;
import java.util.List;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.gateway.config.base.DynamicObjectConfig;


/**
 * 消息读取器配置
 * 
 * @author 刘恭亮
 * 
 */
public class ReaderConfig extends DynamicObjectConfig {
	private List filterConfigList = new ArrayList();

	public List getFilterConfigList() {
		return filterConfigList;
	}

	public List createFilterList() {
		List list = new ArrayList();
		for (int i = 0; i < filterConfigList.size(); i++) {
			String filterClassName = ((FilterConfig) filterConfigList.get(i))
					.getClassName();
			filterClassName = filterClassName.trim();
			AbstractMessageFilter filter = (AbstractMessageFilter) ClassUtil
					.createClassInstance(filterClassName);
			list.add(filter);
		}
		return list;
	}

}
