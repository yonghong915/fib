package com.fib.netty.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.fib.autoconfigure.disruptor.executor.Executor;
import com.fib.netty.util.Session;
import com.fib.netty.vo.Request;
import com.fib.netty.vo.Response;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
@Component
public class NettyServerHandler extends AbstractNettyServerHandler {
	public static long getRandom() {
		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			return secureRandom.nextInt(1000);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	Executor newExecutor(Session session, Object msg) {
		return x -> {
			if (null == msg) {
				throw new NullPointerException("request");
			}
			if (msg instanceof Request request) {
				long sleep = getRandom();
				try {
					TimeUnit.MILLISECONDS.sleep(sleep);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				long id = request.getId();
				Response response = new Response();
				response.setId(id);
				response.setResult("Server Response Ok." + " handle sleep times:" + sleep + " ms "
						+ LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyy-MM-dd HH:mm:ss,SSS"));
				session.writeAndFlush(response);
			}
		};
	}
}