package com.fib.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fib.gateway.netty.client.ConnectionManager;
import com.fib.gateway.netty.client.NettyClient;
import com.fib.gateway.netty.disruptor.MessageConsumer;
import com.fib.gateway.netty.disruptor.RingBufferWorkerPoolFactory;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.disruptor.impl.MessageConsumerClient;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import cn.hutool.core.lang.UUID;
import io.netty.channel.ChannelFuture;

public class NettyTest {

	public static void main(String[] args) {

		MessageConsumer[] conusmers = new MessageConsumer[1];
		for (int i = 0; i < conusmers.length; i++) {
			MessageConsumer messageConsumer = new MessageConsumerClient("code:clientId:" + i);
			conusmers[i] = messageConsumer;
		}
		RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024,
				// new YieldingWaitStrategy(),
				new BlockingWaitStrategy(), conusmers);
		String host = "127.0.0.1";
		int port = 9876;

		int i = 1;
		TranslatorData request = new TranslatorData();
		request.setId(UUID.fastUUID().toString());
		request.setName("请求消息名称 " + i);
		request.setMessage("请求消息内容 " + i);
		NettyClient nettyClient = null;
		long start = System.currentTimeMillis();

		nettyClient = NettyClient.getInstance();
		// ChannelFuture cf = nettyClient.getChannelFuture(host, port);

		nettyClient.connect(host, port);
		nettyClient.sendMessage(request);

//		List<String> nodes = new ArrayList<>();
//		nodes.add("127.0.0.1:" + port);
//		ConnectionManager mg = ConnectionManager.getInstance();
//
//		for (int i = 0; i < 2; i++) {
//			request = new TranslatorData();
//			request.setId(UUID.fastUUID().toString());
//			request.setName("请求消息名称 " + i);
//			request.setMessage("请求消息内容 " + i);
//			// cf.channel().writeAndFlush(request);
//			TranslatorData aa = null;
//			System.out.println("aaa====" + aa);
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		try {
//			cf.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		long end = System.currentTimeMillis();
		System.out.println("duration=" + (end - start));
	}

}
