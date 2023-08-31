package com.fib.netty;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JmeterSocketTest extends AbstractJavaSamplerClient {
	private SampleResult results;
	private String ipAddress;
	private String port;
	private String data;

	/**
	 * 初始化方法
	 */
	@Override
	public void setupTest(JavaSamplerContext jsc) {
		results = new SampleResult();

		ipAddress = jsc.getParameter("ipAddress", "");
		port = jsc.getParameter("port", "");
		data = jsc.getParameter("data", "");

		if (ipAddress != null && ipAddress.length() > 0) {
			results.setSamplerData(ipAddress);
		}

		if (port != null && port.length() > 0) {
			results.setSamplerData(port);
		}

		if (data != null && data.length() > 0) {
			results.setSamplerData(data);
		}

	}

	/**
	 * 设置传入参数
	 */
	@Override
	public Arguments getDefaultParameters() {
		Arguments params = new Arguments();

		params.addArgument("ipAddress", "");
		params.addArgument("port", "");
		params.addArgument("data", "");

		return params;
	}

	/**
	 * 线程运行体
	 */
	public SampleResult runTest(JavaSamplerContext arg0) {
		SocketClient sc = new SocketClient();
		int port2 = 50001;
		if (port != null && port.length() > 0) {
			port2 = Integer.parseInt(port);
		}

		results.sampleStart();
		String rspData = sc.sendMessage(ipAddress, port2, data);
		results.sampleEnd();

		results.setSuccessful(null != rspData && !"".equals(rspData));
		results.setResponseData(rspData, "UTF-8");
		return results;
	}

	/**
	 * 结束方法
	 */
	@Override
	public void teardownTest(JavaSamplerContext arg0) {
		//
	}
}