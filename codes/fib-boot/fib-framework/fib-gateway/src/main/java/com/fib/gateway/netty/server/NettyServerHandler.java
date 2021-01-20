package com.fib.gateway.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.gateway.netty.disruptor.MessageProducer;
import com.fib.gateway.netty.disruptor.RingBufferWorkerPoolFactory;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.service.BaseService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * Server Handler
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
@Service("nettyServerHandler")
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final Map<String, Channel> channels = new ConcurrentHashMap<String, Channel>(); // <ip:port, channel>

	/** 空闲次数 */
	private int idleCount = 1;
	/** 发送次数 */
	private int count = 1;

	@Resource
	private BaseService baseService;

	/**
	 * 建立连接时，发送一条消息
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("connecting the client ip address:{}", ctx.channel().remoteAddress());
		super.channelActive(ctx);
	}

	/**
	 * 超时处理 如果5秒没有接受客户端的心跳，就触发; 如果超过两次，则直接关闭;
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (obj instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) obj;
			if (IdleState.READER_IDLE.equals(event.state())) { // 如果读通道处于空闲状态，说明没有接收到心跳命令
				logger.info("已经5秒没有接收到客户端的信息了");
				if (idleCount > 1) {
					logger.info("关闭这个不活跃的channel");
					ctx.channel().close();
				}
				idleCount++;
			}
		} else {
			super.userEventTriggered(ctx, obj);
		}
	}

	/**
	 * 业务逻辑处理
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("第{}次,Server received request message :{}", count, msg);
		if (!(msg instanceof TranslatorData)) {
			logger.info("未知数据!{}", msg);
			return;
		}
		String producerId = "code:sessionId:001";
		MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
		try {
			TranslatorData request = (TranslatorData) msg;
			messageProducer.onData(request, ctx);
		} catch (Exception e) {
			logger.error("Server handle request error.", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
		count++;
	}

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exception happened.", cause);
		ctx.close();
	}

	public Map<String, Channel> getChannels() {
		return channels;
	}
}
