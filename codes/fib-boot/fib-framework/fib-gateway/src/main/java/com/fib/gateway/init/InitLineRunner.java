package com.fib.gateway.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fib.gateway.CommGateway;
import com.fib.gateway.classloader.AppendableURLClassloader;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-05
 */
@Component
@Data
public class InitLineRunner implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(InitLineRunner.class);

	private String gatewayId;

	private String configPath;

	private String deployPath;

	private ClassLoader classLoader;

	@Override
	public void run(String... args) throws Exception {
		logger.info("InitLineRunner....");
		deployPath = Thread.currentThread().getContextClassLoader().getResource("config/upp/deploy/").getPath();
		gatewayId = "cnaps2";
		init();
		startGateway();
	}

	private void startGateway() {
		CommGateway gateway = new CommGateway();
		gateway.setId(gatewayId);
		gateway.setDeployPath(deployPath);

		gateway.start();
	}

	private void init() {
		logger.info("init....");
		createClassLoader();
	}

	private void createClassLoader() {
		logger.info("createClassLoader....");
		AppendableURLClassloader cl = new AppendableURLClassloader(Thread.currentThread().getContextClassLoader());
		classLoader = cl;
		Thread.currentThread().setContextClassLoader(cl);
	}

}
