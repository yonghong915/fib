package com.fib.netty.codec;

import java.util.List;

import org.apache.dubbo.rpc.model.ApplicationModel;

import com.fib.commons.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-18 10:40:06
 */
public class ProtoStuffDecoder extends ByteToMessageDecoder {
	private Class<?> genericClass;

	public ProtoStuffDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);

		Serializer serializer = ApplicationModel.defaultModel().getDefaultModule().getExtension(Serializer.class,
				"protoStuff");
		out.add(serializer.deserialize(data, genericClass));
	}
}