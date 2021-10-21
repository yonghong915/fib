package com.fib.commons;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.serializer.Serializer;
import com.fib.commons.util.constant.EnumConstants;

public class DubboSPITest {
	private static final Logger logger = LoggerFactory.getLogger(DubboSPITest.class);

	@Test
	public void testProtoStuff() {
		Serializer serializer = ScopeModelUtil.getExtensionLoader(Serializer.class, null).getDefaultExtension();
		System.out.println(serializer);

		serializer = ScopeModelUtil.getExtensionLoader(Serializer.class, null)
				.getExtension(EnumConstants.SerializeType.JDK.getName());
		logger.info(serializer.toString());
	}

}
