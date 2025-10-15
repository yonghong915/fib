package com.fib.netty;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.fib.commons.serializer.Serializer;
import com.fib.commons.serializer.protostuff.ProtoStuffSerializer;
import com.fib.netty.vo.Request;
import com.fib.netty.vo.Response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class SocketClient {

	public static void main(String[] args) {
		final String ip = "127.0.0.1";
		final int port = 8088;
		String data = "hello";
		long start = System.currentTimeMillis();
		SocketClient test = new SocketClient();
		String rspData = test.sendMessage(ip, port, data);
		long end = System.currentTimeMillis();
		System.out.println("rspData=" + rspData + " duration=" + (end - start) + " ms");
	}

	public String sendMessage(String ip, int port, String sourceData) {
		try (Socket socket = new Socket(ip, port);) {
			socket.setSoTimeout(20000);
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();

			Request req = new Request();
			req.setRequestMsg(sourceData);
			Serializer serializer = new ProtoStuffSerializer();
			byte[] data = serializer.serialize(req);

			ByteBuf byteBuf = Unpooled.buffer();

			byteBuf.writeInt(data.length);
			byteBuf.writeBytes(data);
			out.write(byteBuf.array());
			out.flush();

			DataInputStream dataInputStream = new DataInputStream(in);
			int len = dataInputStream.readInt();
			byte[] data1 = new byte[len];
			dataInputStream.readFully(data1);

			ByteBuf buf = Unpooled.wrappedBuffer(data1);
			buf.markReaderIndex();
			int dataLength = buf.readableBytes();
			byte[] data11 = new byte[dataLength];
			buf.readBytes(data11);

			Response rsp = serializer.deserialize(data11, Response.class);
			return rsp.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
