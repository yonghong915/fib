package com.fib.gateway.netty.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <pre>
 *  
 * 自己定义的协议 
 *  数据包格式 
 * +——----——+——-----——+——----——+ 
 * |协议开始标志|  长度             |   数据       | 
 * +——----——+——-----——+——----——+ 
 * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76 
 * 2.传输数据的长度contentLength，int类型 
 * 3.要传输的数据,长度不应该超过2048，防止socket流的攻击
 * </pre>
 */
public class CustomMessageDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(CustomMessageDecoder.class);
	/**
	 * <pre>
	 *  
	 * 协议开始的标准head_data，int类型，占据4个字节. 
	 * 表示数据的长度contentLength，int类型，占据4个字节.
	 * </pre>
	 */
	public static final int BASE_LENGTH = 4 + 4;

	private Class<?> genericClass;
	private Serializer serializer;

	public CustomMessageDecoder(Class<?> genericClass, Serializer serializer) {
		this.genericClass = genericClass;
		this.serializer = serializer;
	}

	@Override
	public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
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
		Object obj = null;
		try {
			obj = serializer.deserialize(data, genericClass);
			out.add(obj);
		} catch (Exception ex) {
			logger.error("Decode error: " + ex.toString());
		}
	}
//
//	@Override
//	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
//		// 可读长度必须大于基本长度
//		if (buffer.readableBytes() >= BASE_LENGTH) {
//			// 防止socket字节流攻击
//			// 防止，客户端传来的数据过大
//			// 因为，太大的数据，是不合理的
//			if (buffer.readableBytes() > 2048) {
//				buffer.skipBytes(buffer.readableBytes());
//			}
//
//			// 记录包头开始的index
//			int beginReader;
//
//			while (true) {
//				// 获取包头开始的index
//				beginReader = buffer.readerIndex();
//				// 标记包头开始的index
//				buffer.markReaderIndex();
//				// 读到了协议的开始标志，结束while循环
//				if (buffer.readInt() == ConstantValue.HEAD_DATA) {
//					break;
//				}
//
//				// 未读到包头，略过一个字节
//				// 每次略过，一个字节，去读取，包头信息的开始标记
//				buffer.resetReaderIndex();
//				buffer.readByte();
//
//				// 当略过，一个字节之后，
//				// 数据包的长度，又变得不满足
//				// 此时，应该结束。等待后面的数据到达
//				if (buffer.readableBytes() < BASE_LENGTH) {
//					return;
//				}
//			}
//
//			// 消息的长度
//
//			int length = buffer.readInt();
//			// 判断请求数据包数据是否到齐
//			if (buffer.readableBytes() < length) {
//				// 还原读指针
//				buffer.readerIndex(beginReader);
//				return;
//			}
//
//			// 读取data数据
//			byte[] data = new byte[length];
//			buffer.readBytes(data);
//
//			CustomProtocol protocol = new CustomProtocol(data.length, data);
//			out.add(protocol);
//		}
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("level call exception .....");
		cause.printStackTrace();
		ctx.close();
	}

}
