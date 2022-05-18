package com.fib.netty.server;

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

	@Override
	Executor newExecutor(Session session, Object msg) {
		return x -> {
			if (null == msg) {
				throw new NullPointerException("request");
			}
			if (msg instanceof Request request) {
				long id = request.getId();
				Response response = new Response();
				response.setId(id);
				response.setResult("Server Response Ok." + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyy-MM-dd HH:mm:ss,SSS"));
				session.writeAndFlush(response);
			}
		};
	}
}