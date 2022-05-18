package com.fib.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.disruptor.DisruptorTemplate;
import com.fib.autoconfigure.disruptor.executor.Executor;
import com.fib.netty.util.DefaultSession;
import com.fib.netty.util.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 09:36:17
 */
@Component
public abstract class AbstractNettyServerHandler extends ChannelInboundHandlerAdapter {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyServerHandler.class);
	protected static final Map<ChannelId, Session> SESSIONS = new ConcurrentHashMap<>();

	@Autowired
	private DisruptorTemplate disruptorTemplate;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("Client {} connected", ctx.channel().remoteAddress());
		DefaultSession session = new DefaultSession(ctx);
		SESSIONS.put(ctx.channel().id(), session);
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		LOGGER.info("The Server received msg is : [{}]", msg);
		Session session = SESSIONS.get(ctx.channel().id());
		if (null == session) {
			return;
		}
		pushMessage(ctx, msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Session session = SESSIONS.remove(ctx.channel().id());
		if (null != session) {
			session.release();
		}
		super.channelInactive(ctx);
	}

	/**
	 * 业务处理器
	 * 
	 * @param session
	 * @param msg
	 * @return
	 */
	abstract Executor newExecutor(Session session, Object msg);

	private void pushMessage(ChannelHandlerContext ctx, Object msg) {
		Session session = SESSIONS.get(ctx.channel().id());
		if (null == session) {
			return;
		}
		disruptorTemplate.publishEvent(newExecutor(session, msg), msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error("Unexpected exception from downstream.", cause);
		ctx.fireExceptionCaught(cause);
		ctx.close();
	}
}
