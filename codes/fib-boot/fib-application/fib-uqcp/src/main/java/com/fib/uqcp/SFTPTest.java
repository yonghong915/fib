package com.fib.uqcp;

import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPTest {
	private Session session = null;
	private ChannelSftp sftp = null;

	public void initSession(String sHost, int iPort, String sUserName, String sPassword) throws JSchException {
		// JSch库来建立与SFTP服务器的连接
		JSch jSch = new JSch();
		this.session = jSch.getSession(sUserName, sHost, iPort);// getSession()建立与sftp服务器的会话
		this.session.setPassword(sPassword);
		this.session.setTimeout(30000);

		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		this.session.setConfig(sshConfig);

		this.session.connect();// 连接sftp服务器

	}

	public void initChannelSftp() throws JSchException {
		// 打开一个sftp通道，并将其转化为ChannelSftp对象
		this.sftp = (ChannelSftp) this.session.openChannel("sftp");
		this.sftp.connect();
		System.out.println("Connected to SFTP Server.");

//		String remotePath = "up/";
//		try {
//			this.sftp.cd(remotePath);
//			this.sftp.mkdir(remotePath);
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void put() {
		try {
			this.sftp.put("e:/test.txt", "up/");
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		this.sftp.disconnect();
		this.session.disconnect();
	}

	public static void main(String[] args) {
		SFTPTest test = new SFTPTest();
		String host = "192.168.56.11";
		int port = 22;
		String username = "sftp_upload";
		String pwd = "123456";
		try {
			test.initSession(host, port, username, pwd);
			test.initChannelSftp();

			test.put();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			test.close();
		}
	}

}
