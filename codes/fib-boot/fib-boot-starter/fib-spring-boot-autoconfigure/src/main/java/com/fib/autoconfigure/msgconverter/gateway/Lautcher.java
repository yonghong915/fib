package com.fib.autoconfigure.msgconverter.gateway;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.msgconverter.MsgconverterProperties;
import com.fib.commons.classloader.CustomClassLoader;

/**
 * 启动器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 17:30:22
 */
@Component
public class Lautcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(Lautcher.class);

	@Autowired
	private CommGateway commGateway;

	@Autowired
	private MsgconverterProperties msgconverterProperties;
	private ClassLoader classLoader;

	/**
	 * 
	 */
	public void init() {
		LOGGER.info("init...");
		createClassLoader();
		start();
	}

	private void createClassLoader() {
		List<URL> jarUrlList = new ArrayList<>(64);
		// TODO
		jarUrlList.add(this.getClass().getClassLoader().getResource(""));
		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		CustomClassLoader cl = new CustomClassLoader(urls, Thread.currentThread().getContextClassLoader());
		classLoader = cl;
		Thread.currentThread().setContextClassLoader(cl);

	}

	/**
	 * 
	 */
	private void start() {
		commGateway.setId(msgconverterProperties.getGatewayId());
		commGateway.setDeployPath(msgconverterProperties.getDeployPath());
		commGateway.start();
	}
}