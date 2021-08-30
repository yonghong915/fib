package com.fib.msgconverter;

public class SFTPTest {

	public static void main(String[] args) {
		String host = "172.24.0.121";

		String username = "lstest";

		String password = "lstest";
		SftpClient client = new SftpClient(host, username, password);
		client.connect();
	}

}
