package com.fib.netty.codec;

import org.apache.dubbo.rpc.model.ApplicationModel;

import com.fib.commons.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 10:41:05
 */
public class ProtoStuffEncoder extends MessageToByteEncoder<Object> {
	private Class<?> genericClass;

	public ProtoStuffEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) {
		if (genericClass.isInstance(in)) {
			Serializer serializer = ApplicationModel.defaultModel().getDefaultModule().getExtension(Serializer.class,
					"protoStuff");
			byte[] data = serializer.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
}