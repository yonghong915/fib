package com.fib.ecny.trans.comp.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 初始化Disruptor
        MsgDisruptorManager disruptorManager = new MsgDisruptorManager();
        disruptorManager.start();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 管道顺序：解码(分包) -> 业务处理器 -> 编码
                            ch.pipeline()
                                    // 第一步：长度域解码器，自动解决粘包拆包
                                    .addLast(new ProtostuffDecoder())
                                    // 业务转发handler
                                    .addLast(new NettyMsgHandler(disruptorManager))
                                    // 出站编码
                                    .addLast(new ProtostuffEncoder());
                        }
                    });
            ChannelFuture f = b.bind(8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            disruptorManager.shutdown();
        }
    }
}
