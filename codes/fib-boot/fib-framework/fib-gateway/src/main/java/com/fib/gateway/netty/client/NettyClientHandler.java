package com.fib.gateway.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.gateway.netty.disruptor.MessageProducer;
import com.fib.gateway.netty.disruptor.RingBufferWorkerPoolFactory;
import com.fib.gateway.netty.disruptor.TranslatorData;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
@Sharable
public class NettyClientHandler extends ChannelDuplexHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ConcurrentHashMap<String, SynchronousQueue<Object>> map = new ConcurrentHashMap<>();
	private ChannelHandlerContext ctx;
	private ChannelPromise promise;
	private TranslatorData response;

	// ConcurrentHashMap<Long, BlockingQueue<Response>> responseMap = new
	// ConcurrentHashMap<>();
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Client Active ,Connected to Server.....{}", ctx.channel().remoteAddress());
		this.ctx = ctx;
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("disconnected to Server,ip:{}.", ctx.channel().remoteAddress());
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Client receive message: {}", msg); 
		NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
		if (!(msg instanceof TranslatorData)) {
			logger.info("Client received message's form is incorrect.");
			return;
		}
		// 用于获取客户端发来的数据信息
		try {
			TranslatorData resp = (TranslatorData) msg;
			String producerId = "code:seesionId:002";
			MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
			messageProducer.onData(resp, ctx);
//			response = resp;
//			promise.setSuccess();
//			String requestId = resp.getId();
//			SynchronousQueue<Object> queue = map.get(requestId);
//			queue.put(resp);
//			map.remove(requestId);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			logger.info("已超过30秒未与RPC服务器进行读写操作!将发送心跳消息...");
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
//				RpcRequest request = new RpcRequest();
//				request.setMethodName("heartBeat");
//				log.info("send heart beat!");
//				log.info("active : " + ctx.channel().isActive());
//				ctx.channel().writeAndFlush(Unpooled.buffer().writeBytes(request.toString().getBytes()));
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("RPC通信服务器发生异常.", cause);
		ctx.close();
	}

	public SynchronousQueue<Object> sendRequest(TranslatorData request, Channel channel) {
		SynchronousQueue<Object> queue = new SynchronousQueue<>();
		map.put(request.getId(), queue);
		channel.writeAndFlush(request);
		return queue;
	}

	public synchronized ChannelPromise sendMsg(TranslatorData message) {
		while (ctx == null) {
			try {
				TimeUnit.MILLISECONDS.sleep(1);
				// logger.error("等待ChannelHandlerContext实例化");
			} catch (InterruptedException e) {
				logger.error("等待ChannelHandlerContext实例化过程中出错", e);
			}
		}
		promise = ctx.newPromise();
		ctx.writeAndFlush(message);
		return promise;
	}

	public TranslatorData getResponse() {
		return response;
	}
}