package com.fib.ecny.trans.comp.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyMsgHandler extends SimpleChannelInboundHandler<MsgDto> {
    private final MsgDisruptorManager disruptorManager;

    public NettyMsgHandler(MsgDisruptorManager disruptorManager) {
        this.disruptorManager = disruptorManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgDto msg) {
        // IO线程快速投递到Disruptor，立刻释放IO线程
        disruptorManager.publish(ctx.channel(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
