package com.fib.ecny.trans.comp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtostuffDecoder extends LengthFieldBasedFrameDecoder {
    // 协议配置：长度头4字节，长度偏移0，长度占用4字节，跳过头部4字节
    private static final int LENGTH_FIELD_OFFSET = 0;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 4;

    public ProtostuffDecoder() {
        // 最大单包10MB，防止超大报文攻击
        super(1024 * 1024 * 10, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) return null;

        byte[] body = new byte[frame.readableBytes()];
        frame.readBytes(body);
        // 泛型可自行封装消息头统一实体，此处示例业务POJO
        return ProtostuffUtil.deserialize(body, MsgDto.class);
    }
}
