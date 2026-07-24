package com.fib.ecny.trans.comp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtostuffEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        byte[] data = ProtostuffUtil.serialize(msg);
        // 先写入4字节长度（大端）
        out.writeInt(data.length);
        // 写入实体二进制
        out.writeBytes(data);
    }
}
