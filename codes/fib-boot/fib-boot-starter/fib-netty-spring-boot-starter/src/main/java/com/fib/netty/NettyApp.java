package com.fib.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.dubbo.rpc.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.fib.commons.serializer.Serializer;
import com.fib.netty.client.NettyClient;
import com.fib.netty.server.NettyServer;

@SpringBootApplication
public class NettyApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyApp.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(NettyApp.class, args);
		NettyServer nettyServer = ctx.getBean(NettyServer.class);
		nettyServer.start();

		NettyClient client = ctx.getBean(NettyClient.class);
		ExecutorService executor = Executors.newFixedThreadPool(10);

		try {
			ApplicationModel.defaultModel().getDefaultModule().getExtension(Serializer.class, "protoStuff");

			long start = System.currentTimeMillis();
			client.connect("127.0.0.1", 8088);
			AtomicLong cnt = new AtomicLong(0);
			List<CompletableFuture<Object>> completableFutures = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				CompletableFuture<Object> result = CompletableFuture.supplyAsync(() -> {
					Object rtnObj = null;
					try {
						rtnObj = client.send("hello netty." + cnt.incrementAndGet());
						LOGGER.info("rtnObj={}", rtnObj);
						return rtnObj;
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					return rtnObj;
				}, executor);
				completableFutures.add(result);
			}
			CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join();

			LOGGER.info("duration is {} ms.", (System.currentTimeMillis() - start));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}