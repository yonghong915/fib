package com.fib.tcp.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class SocketClient {
	public static void main(String[] args) {
		final String DEFAULT_SERVER_HOST = "127.0.0.1";
		final int DEFAULT_SERVER_PORT = 8000;

		try (// 创建socket
				final Socket socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);) {
			// 创建IO流
			final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ExecutorService es = Executors.newFixedThreadPool(10);
			final AtomicLong cnt = new AtomicLong();

			for (int i = 0; i < 10; i++) {
				es.submit(() -> {
					try {
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						writer.write("socket" + cnt.incrementAndGet());
						writer.flush();
						// 读取服务器返回的消息
						String msg = reader.readLine();
						System.out.println(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

//			try {
//				writer.close(); // 关闭之前还会flush一次
//				socket.close();
//				reader.close();
//				consoleReader.close();
//				System.out.println("关闭socket");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		}
	}
}
