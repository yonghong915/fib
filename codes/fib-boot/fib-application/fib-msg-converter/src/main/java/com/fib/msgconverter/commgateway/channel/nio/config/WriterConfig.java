/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午03:28:10
 */
package com.fib.msgconverter.commgateway.channel.nio.config;

import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.giantstone.common.util.ClassUtil;

/**
 * 消息发送器配置
 * @author 刘恭亮
 *
 */
public class WriterConfig extends DynamicObjectConfig {
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
			AbstractMessageFilter filter = (AbstractMessageFilter) ClassUtil.createClassInstance(filterClassName);
			list.add(filter);
		}
		return list;
	}

 }
