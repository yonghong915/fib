package com.fib.gateway.netty.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.Serializer;
import com.fib.commons.serializer.protostuff.ProtoStuffSerializer;
import com.fib.gateway.netty.disruptor.TranslatorData;
import com.fib.gateway.netty.protocol.CustomMessageDecoder;
import com.fib.gateway.netty.protocol.CustomMessageEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
public class NettyClient extends AbstractClient {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Bootstrap bootstrap;

	private ConnectionManager manage = ConnectionManager.getInstance();
	private EventLoopGroup workerGroup;

	public ConnectionManager getMgr() {
		return manage;
	}

	private static class SingletonHolder {
		static final NettyClient instance = new NettyClient();
	}

	public static NettyClient getInstance() {
		return SingletonHolder.instance;
	}

	public NettyClient() {
		super();
	}

	@Override
	protected void doOpen() {
		workerGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("NettyClientWorker", true));
		bootstrap = new Bootstrap().group(workerGroup).channel(NioSocketChannel.class)
				// 该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
				.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.option(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				Serializer serializer = SerializationUtils.getInstance()
						.loadSerializerInstance(ProtoStuffSerializer.class);
				ChannelPipeline cp = socketChannel.pipeline();
				cp.addLast(new IdleStateHandler(0, 0, 1000, TimeUnit.SECONDS));
				cp.addLast(new CustomMessageEncoder(TranslatorData.class, serializer));
				cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
				cp.addLast(new CustomMessageDecoder(TranslatorData.class, serializer));

				// 超时handler（当服务器端与客户端在指定时间以上没有任何进行通信，则会关闭响应的通道，主要为减小服务端资源占用）
				cp.addLast(new ReadTimeoutHandler(100));
				cp.addLast(new NettyClientHandler());
			}
		});
	}

	// 扩展 完善 池化: ConcurrentHashMap<KEY -> String, Value -> Channel>
	private volatile Channel channel;

	private ChannelFuture cf;

	public void connect(String host, int port) {
		try {
			this.cf = bootstrap.connect(host, port).sync();
			logger.info("Client connected....");
			// 接下来就进行数据的发送, 但是首先我们要获取channel:
			this.channel = this.cf.channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(TranslatorData request) {
		ChannelFuture cf = this.channel.writeAndFlush(request);
		try {
			boolean success = cf.await(1000);
			System.out.println("success=" + success);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ChannelFuture getChannelFuture(String host, int port) {
		if (this.cf == null) {
			this.connect(host, port);
		}
		if (!this.cf.channel().isActive()) {
			this.connect(host, port);
		}
		return this.cf;
	}

	public void close() {
		try {
			this.cf.channel().closeFuture().sync();
			// 优雅停机
			this.workerGroup.shutdownGracefully();
			logger.info("Sever ShutDown...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Channel getChannel() {
		return null;
	}

	@Override
	protected void doClose() {
		// TODO Auto-generated method stub

	}

	long connectTimeout = 3000l;

	@Override
	protected void doConnect() {
		ChannelFuture future = bootstrap.connect(getConnectAddress());
		boolean ret = future.awaitUninterruptibly(connectTimeout, TimeUnit.MILLISECONDS);
		if (ret && future.isSuccess()) {
			Channel newChannel = future.channel();
			Channel oldChannel = NettyClient.this.channel;
			if (oldChannel != null) {
				oldChannel.close();
			}
			NettyClient.this.channel = newChannel;
		}
	}

	@Override
	protected void doDisConnect() {

	}

//	public Channel doChannel(SocketAddress address) throws InterruptedException {
//		// ChannelFuture f = bootstrap.connect(address);
//		// return f.sync().channel();
//		return this.channel;
////      return channel;
//	}

	/**
	 * 客户端发送消息
	 * 
	 * @param message
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
//	public TranslatorData sendMsg(NettyMessage message) throws InterruptedException, ExecutionException {
//		ChannelPromise promise = clientHandler.sendMessage(message);
//		promise.await(3, TimeUnit.SECONDS);
//		return clientHandler.getResponse();
//	}
}
