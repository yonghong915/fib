package com.fib.msgconverter.commgateway.job.impl;

import com.fib.msgconverter.commgateway.job.AbstractJob;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ClassUtil;

/**
 * 本地任务代理
 * 
 * @author 白帆
 * 
 */
public class LocalJobDelegator extends AbstractJob {
	// 本地处理类的处理方法名
	public static final String EXCUTE_METHOD_NAME = "execute";

	protected boolean businessLogic() {
		String jobClassName = currentJobInfo.getJobClassName();
		if (null == jobClassName) {
			// throw new RuntimeException("Local Job Class Name is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"LocalJobDelegator.businessLogic.className.null"));
		}
		Object jobClazz = ClassUtil.createClassInstance(jobClassName);

		boolean isSuccess = Boolean.valueOf(String.valueOf(ClassUtil.invoke(
				jobClazz, EXCUTE_METHOD_NAME, null, null)));

		return isSuccess;
	}

}
