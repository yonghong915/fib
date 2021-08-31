package com.fib.msgconverter.commgateway.channel.nio.config;

import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.giantstone.common.util.ClassUtil;

/**
 * 消息读取器配置
 * 
 * @author 刘恭亮
 * 
 */
public class ReaderConfig extends DynamicObjectConfig {
	private List<FilterConfig> filterConfigList = new ArrayList<>();

	public List<FilterConfig> getFilterConfigList() {
		return filterConfigList;
	}

	public List<AbstractMessageFilter> createFilterList() {
		List<AbstractMessageFilter> list = new ArrayList<>();
		for (int i = 0; i < filterConfigList.size(); i++) {
			String filterClassName = (filterConfigList.get(i)).getClassName();
			filterClassName = filterClassName.trim();
			AbstractMessageFilter filter = (AbstractMessageFilter) ClassUtil.createClassInstance(filterClassName);
			list.add(filter);
		}
		return list;
	}
}