package com.fib.msgconverter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SftpClient {
	private final static Logger logger = LoggerFactory.getLogger(SftpClient.class);

	private static String host = "172.24.0.121";

	private static String username = "lstest";

	private static String password = "lstest";

	protected static String privateKey;// 密钥文件路径

	protected static String passphrase;// 密钥口令

	private static int port = 22;

	private static ChannelSftp sftp = null;

	private static Session sshSession = null;

	public SftpClient(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public SftpClient(String host, String username, String password, int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public SftpClient(String host, String username, String password, int port, String privateKey, String passphrase) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.port = port;
	}

	public static void connect() {
		JSch jsch = new JSch();
		Channel channel = null;
		try {
			if (!StringUtils.isEmpty(privateKey)) {
				// 使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
				if (!StringUtils.isEmpty(passphrase)) {
					jsch.addIdentity(privateKey, passphrase);
				} else {
					jsch.addIdentity(privateKey);
				}
			}
			sshSession = jsch.getSession(username, host, port);
			if (!StringUtils.isEmpty(password)) {
				sshSession.setPassword(password);
			}
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");// do not verify host
															// key
			sshSession.setConfig(sshConfig);
			// session.setTimeout(timeout);
			// session.setServerAliveInterval(92000);
			sshSession.connect();
			// 参数sftp指明要打开的连接是sftp连接
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			logger.error("连接【" + host + ":" + port + "】异常", e);
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return channel
	 */
	public void connectWithException() throws Exception {
		JSch jsch = new JSch();
		Channel channel = null;
		try {
			if (!StringUtils.isEmpty(privateKey)) {
				// 使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
				if (!StringUtils.isEmpty(passphrase)) {
					jsch.addIdentity(privateKey, passphrase);
				} else {
					jsch.addIdentity(privateKey);
				}
			}
			sshSession = jsch.getSession(username, host, port);
			if (!StringUtils.isEmpty(password)) {
				sshSession.setPassword(password);
			}
			Properties sshConfig = new Properties();
			// do not verify host key
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			// session.setTimeout(timeout);
			// session.setServerAliveInterval(92000);
			sshSession.connect();
			// 参数sftp指明要打开的连接是sftp连接
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			logger.error("连接【" + host + ":" + port + "】异常", e);
			throw new Exception("Could not connect to :" + host + ":" + port);
		}
	}

	/**
	 * 关闭资源
	 */
	public static void disconnect() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
		}
		if (sshSession != null) {
			if (sshSession.isConnected()) {
				sshSession.disconnect();
			}
		}
	}

	/**
	 * sftp is connected
	 *
	 * @return
	 */
	public static boolean isConnected() {
		return sftp != null && sftp.isConnected();
	}

	public InputStream downFile(String remotePath, String remoteFile) throws Exception {
		try {
			if (sftp == null)
				connect();
			sftp.cd(remotePath);
			return sftp.get(remoteFile);
		} catch (SftpException e) {
			logger.error("文件下载失败或文件不存在！", e);
			throw e;
		} finally {
			// disconnect();
		}
	}

	public byte[] downLoad(String remotePath, String remoteFile) throws Exception {
		try {
			if (sftp == null)
				connect();
			sftp.cd(remotePath);
			InputStream input = sftp.get(remoteFile);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[10485760];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} catch (SftpException e) {
			logger.error("文件下载失败或文件不存在！", e);
		} finally {
			disconnect();
		}
		return null;
	}

	public static void main(String[] args) {
		downloadFile("/home/lstest/dwn/dwnnc", "20210921171729", "D:/work/", "20.txt");
	}

	/**
	 * 下载单个文件
	 *
	 * @param remoteFileName 下载文件名
	 * @param localPath      本地保存目录(以路径符号结束)
	 * @param localFileName  保存文件名
	 * @return
	 */
	public static synchronized boolean downloadFile(String remotePath, String remoteFileName, String localPath,
			String localFileName) {
		logger.info(remotePath + "/" + remoteFileName + "/" + localPath + "/" + localFileName);
		try {
			if (sftp == null || !isConnected()) {
				connect();
			}
			sftp.cd(remotePath);
			File file = new File(localPath + localFileName);
			mkdirs(localPath + localFileName);
			sftp.get(remoteFileName, new FileOutputStream(file));
			return true;
		} catch (FileNotFoundException e) {
			logger.error("不存在文件,Path:" + remotePath + ",file:" + remoteFileName, e);
		} catch (SftpException e) {
			logger.error("下载文件处理异常,Path:" + remotePath + ",file:" + remoteFileName, e);
		} finally {
			disconnect();
		}
		return false;
	}

	/**
	 * 上传
	 */
	public void uploadFile(String remotePath, String fileName, InputStream input) throws IOException, Exception {
		try {
			if (sftp == null) {
				connect();
			}
			// createDir(remotePath);
			mkDir(remotePath.replace(sftp.pwd(), ""));// 绝对路径变为相对路径
			sftp.put(input, fileName);
		} catch (Exception e) {
			logger.error("文件上传异常！", e);
			throw new Exception("文件上传异常:" + e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
				}
			}
			// disconnect();
		}
	}

	/**
	 * 上传单个文件
	 *
	 * @param remotePath     远程保存目录
	 * @param remoteFileName 保存文件名
	 * @param localPath      本地上传目录(以路径符号结束)
	 * @param localFileName  上传的文件名
	 * @return
	 */
	public boolean uploadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
		File fileInput = new File(localPath + localFileName);
		return uploadFile(remotePath, remoteFileName, fileInput);
	}

	/**
	 * 上传单个文件
	 *
	 * @param remotePath     远程保存目录
	 * @param remoteFileName 保存文件名
	 * @param fileInput      上传的文件
	 * @return
	 */
	public boolean uploadFile(String remotePath, String remoteFileName, File fileInput) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileInput);
			uploadFile(remotePath, remoteFileName, in);
			return true;
		} catch (Exception e) {
			logger.error("上传单个文件异常", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.warn("关闭sftp资源异常", e);
				}
			}
		}
		return false;
	}

	/**
	 * 创建目录
	 *
	 * @param createpath
	 * @return
	 */
	private boolean createDir(String createpath) {
		try {
			if (isDirExist(createpath)) {
				this.sftp.cd(createpath);
				return true;
			}
			String[] pathArry = createpath.split("/");
			StringBuffer filePath = new StringBuffer("/");
			for (String path : pathArry) {
				if (path.equals("")) {
					continue;
				}
				filePath.append(path + "/");
				if (isDirExist(filePath.toString())) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}
			}
			return true;
		} catch (SftpException e) {
			logger.error("sftp创建目录异常", e);
		}
		return false;
	}

	/**
	 * 判断目录是否存在
	 *
	 * @param directory
	 * @return
	 */
	public boolean isDirExist(String directory) {
		try {
			SftpATTRS sftpATTRS = sftp.lstat(directory);
			return sftpATTRS.isDir();
		} catch (Exception e) {
			// logger.error("sftp目录isDirExist异常", e);
		}
		return false;
	}

	/**
	 * 如果目录不存在就创建目录
	 *
	 * @param path
	 */
	private static void mkdirs(String path) {
		File f = new File(path);
		String fs = f.getParent();
		f = new File(fs);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	public boolean deleteFile(String remotePath, String remoteFile) throws Exception {
		try {
			if (sftp == null)
				connect();
			if (openDir(remotePath)) {
				sftp.rm(remoteFile);
			}
			return true;
		} catch (SftpException e) {
			logger.error("删除文件【" + remotePath + "/" + remoteFile + "】发生异常！", e);
			throw e;
		}
	}

	/**
	 * 创建文件夹
	 *
	 * @param dirName
	 */
	public void mkDir(String dirName) {
		String[] dirs = dirName.split("/");
		try {
			String now = sftp.pwd();
			for (int i = 0; i < dirs.length; i++) {
				if (Strings.isNullOrEmpty(dirs[i]))
					continue;
				boolean dirExists = openDir(dirs[i]);
				if (!dirExists) {
					sftp.mkdir(dirs[i]);
					sftp.cd(dirs[i]);
				}
			}
			// 进入当前目录
			sftp.cd(now + "/" + dirName);
		} catch (SftpException e) {
			logger.error("mkDir Exception : " + e);
		}
	}

	/**
	 * 打开文件夹一层一层
	 *
	 * @param directory
	 * @return
	 */
	public boolean openDir(String directory) {
		try {
			logger.debug("opendir: {}", directory);
			sftp.cd(directory);
			return true;
		} catch (SftpException e) {
			logger.debug("openDir【" + directory + "】 Exception : " + e);
			return false;
		}
	}

	/**
	 * 获取输出的out put stream
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public OutputStream getUpLoadStream(String path, String name, String rcCode) throws Exception {
		if (sftp == null || !isConnected()) {
			connect();
		}
		String finalPath = path + rcCode;
		synchronized (CREATE_PATH_LOCK) {
			if (!openDir(finalPath)) {
				createDir(finalPath);
			}
		}
		sftp.cd(finalPath);
		return sftp.put(name);
	}

	private static Object CREATE_PATH_LOCK = new Object();

	public OutputStream getUpLoadStream(String path, String name) throws Exception {
		if (sftp == null || !isConnected()) {
			connect();
		}
		synchronized (CREATE_PATH_LOCK) {
			if (!openDir(path)) {
				createDir(path);
			}
		}
		sftp.cd(path);
		return sftp.put(name);
	}

	/**
	 * 上传 不关闭任何流
	 */
	public void uploadFileNotClose(InputStream input, String remotePath, String fileName)
			throws IOException, Exception {
		try {
			if (sftp == null) {
				connect();
			}
			if (!openDir(remotePath)) {
				createDir(remotePath);
			}
			sftp.put(input, fileName);
		} catch (Exception e) {
			logger.error("文件上传异常！", e);
			throw new Exception("文件上传异常:" + e.getMessage());
		}
	}

	/**
	 * 上传(input上传完成,并未关闭,在外层调用处虚处理)
	 */
	public void uploadFile(InputStream input, String remotePath, String fileName) throws IOException, Exception {
		try {
			if (sftp == null) {
				connect();
			}
			if (!openDir(remotePath)) {
				createDir(remotePath);
			}
			sftp.put(input, fileName);
		} catch (Exception e) {
			logger.error("文件上传异常！", e);
			throw new Exception("文件上传异常:" + e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
				}
			}
			disconnect();
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ChannelSftp getSftp() {
		return sftp;
	}

	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}

	@SuppressWarnings("unchecked")
	public Vector<ChannelSftp.LsEntry> listFiles(String remotePath) throws Exception {
		Vector<ChannelSftp.LsEntry> vector = null;
		try {
			if (sftp == null)
				connect();
			sftp.cd("/");
			vector = sftp.ls(remotePath);
		} catch (SftpException e) {
			throw new Exception("list file error.", e);
		}
		return vector;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param directory
	 * @return
	 */
	public boolean isFileExist(String directory) {
		boolean isDirExistFlag = false;
		try {
			if (sftp == null)
				connect();
			sftp.lstat(directory);
			isDirExistFlag = true;
		} catch (Exception e) {
			isDirExistFlag = false;
		}
		return isDirExistFlag;
	}
}
