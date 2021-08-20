package com.fib.commons;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.serializer.Serializer;
import com.fib.commons.util.constant.EnumConstants;

public class DubboSPITest {
	private static final Logger logger = LoggerFactory.getLogger(DubboSPITest.class);

	@Test
	public void testProtoStuff() {
		Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getDefaultExtension();
		System.out.println(serializer);

		serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
				.getExtension(EnumConstants.SerializeType.JDK.getName());
		logger.info(serializer.toString());
	}

}
