package com.fib.midbiz;

import java.util.List;

import cn.hutool.extra.ssh.Sftp;

public class SftpUtil {
	public static void main(String[] args) {
		String host = "192.168.56.11";
		int port = 22;
		String username = "sftpuser";
		String pwd = "sftpuser";
		try (Sftp sftp = new Sftp(host, port, username, pwd)) {
			List<String> ls = sftp.ls("data");
			for (String l : ls) {
				if (l.endsWith("txt")) {
					// sftp.mkDirs("data/bakckup/20250909");
					// sftp.mkdir("data/20250909");
					// sftp.download("data/" + l, new File(l));
					//sftp.rename("data/" + l, "data/20250909");
					// sftp.upload("data", new File("fa.txt"));
				}
				System.out.println(ls.size());
			}
		}
	}
}
