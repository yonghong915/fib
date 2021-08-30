/**
 * 北京长信通信息技术有限公司
 * 2008-9-18 下午02:14:22
 */
package com.fib.msgconverter.commgateway.startup;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.classloader.AppendableURLClassloader;

/**
 * 引导程序
 * 
 * @author 刘恭亮
 * 
 */
public class Bootstrap {
	/**
	 * 核心库
	 */
	public static final String LIB_PATH_CORE = "core";

	/**
	 * 外部库
	 */
	public static final String LIB_PATH_EXT = "ext";

	public static final String CMD_START = "start";
	public static final String CMD_STOP = "stop";
	public static final String CMD_RESET_LOGGER = "reset_logger";

	private String libPath;
	private String configPath;
	private String deployPath;
	private String gatewayId;
	private ClassLoader classLoader;
	private boolean configDBSupport = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String project_name = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\outpro";
		args = new String[5];
		args[0] = project_name + "/lib";
		args[1] = project_name + "/config";
		args[2] = project_name + "/deploy";
		args[3] = "cnaps2";// cnaps2
		args[4] = "start";

		if (args.length != 5 && args.length != 6) {
			printUsage();
			System.exit(0);
		}
		String command = args[args.length - 1];
		if (!(CMD_START.equals(command) || CMD_STOP.equals(command) || CMD_RESET_LOGGER.equals(command))) {
			printUsage();
			System.exit(0);
		}

		Bootstrap bootstrap = new Bootstrap();
		if (args.length == 5) {
			bootstrap.setLibPath(args[0]);
			bootstrap.setConfigPath(args[1]);
			bootstrap.setDeployPath(args[2]);
			bootstrap.setGatewayId(args[3]);
		} else {
			bootstrap.setLibPath(args[0]);
			bootstrap.setConfigPath(args[1]);
			bootstrap.setDeployPath(args[2]);
			bootstrap.setGatewayId(args[3]);
			if ("DB_SUPPORT".equals(args[4])) {
				bootstrap.setConfigDBSupport(true);
			}
		}
		bootstrap.init();
		try {
			if (CMD_START.equals(command)) {
				bootstrap.startGateway();
			} else if (CMD_STOP.equals(command)) {
				bootstrap.stopGateway();
			} else if (CMD_RESET_LOGGER.equals(command)) {
				bootstrap.resetLogger();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}

	}

	private void sendCommand(String command) {
		// 1. get gateway monitor port
		// 1.1. create GatewayConfigParser
		Object gatewayConfig = null;
		Method m = null;
		if (configDBSupport) {
			Class gatewayConfigLoaderClazz = null;
			try {
				gatewayConfigLoaderClazz = classLoader
						.loadClass("com.fib.msgconverter.commgateway.config.database.GatewayConfigLoader");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			Object o = null;
			try {
				o = gatewayConfigLoaderClazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			try {
				m = gatewayConfigLoaderClazz.getMethod("loadConfig", java.lang.String.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			try {
				gatewayConfig = m.invoke(o, gatewayId);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			Class gatewayConfigParserClazz = null;
			try {
				gatewayConfigParserClazz = classLoader
						.loadClass("com.fib.msgconverter.commgateway.config.GatewayConfigParser");
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
			Object o = null;
			try {
				o = gatewayConfigParserClazz.newInstance();
			} catch (Exception e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
			// 1.2. parse
			try {
				m = gatewayConfigParserClazz.getMethod("parse", java.lang.String.class);
			} catch (Exception e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
			try {
				gatewayConfig = m.invoke(o, "gateway_" + gatewayId + ".xml");
			} catch (Exception e) {
				// e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		// 1.3. get monitor port
		try {
			m = gatewayConfig.getClass().getMethod("getMonitorPort");
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		Integer mp = null;
		try {
			mp = (Integer) m.invoke(gatewayConfig);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		int monitorPort = mp.intValue();

		// 2. send command
		Socket s = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			s = new Socket("127.0.0.1", monitorPort);
			s.setSoTimeout(5000);
			out = s.getOutputStream();
			out.write(command.getBytes());

			in = s.getInputStream();
			byte[] res = new byte[1024];
			int onceRead = in.read(res);
			if (-1 == onceRead) {
				System.out.println(command + " failed! onceRead=-1 ! remote connection maybe closed");
				// System.out.println(MultiLanguageResourceBundle.getInstance()
				// .getString("Bootstrap.onceRead.-1",
				// new String[] { command }));
			} else {
				System.out.println(new String(res, 0, onceRead));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	public void resetLogger() {
		sendCommand(CMD_RESET_LOGGER);
	}

	public void stopGateway() {
		sendCommand(CMD_STOP);
	}

	public void startGateway() {
		// Gateway
		Class gatewayClazz = null;
		try {
			gatewayClazz = classLoader.loadClass("com.fib.msgconverter.commgateway.CommGateway");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		Constructor c = null;
		try {
			c = gatewayClazz.getConstructor(java.lang.String.class);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		Object o = null;
		try {
			o = c.newInstance(gatewayId);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		Method m = null;
		if (configDBSupport) {
			try {
				m = gatewayClazz.getMethod("setConfigDBSupport", boolean.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				m.invoke(null, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		// setDeployPath
		try {
			m = gatewayClazz.getMethod("setDeployPath", String.class);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			m.invoke(o, deployPath);
		} catch (Exception e) {
			// e.printStackTrace();
			// stopGateway();
			throw new RuntimeException(e);
		}

		// start
		m = null;
		try {
			m = gatewayClazz.getMethod("start");
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			m.invoke(o);
		} catch (Exception e) {
			// e.printStackTrace();
			// stopGateway();
			throw new RuntimeException(e);
		}

	}

	public void init() {
		// 1. ClassLoader
		createClassLoader();

		// 2. ConfigManager
		Class configManagerClazz = null;
		try {
			configManagerClazz = classLoader.loadClass("com.giantstone.common.config.ConfigManager");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		// setRootPath
		Method m = null;
		try {
			m = configManagerClazz.getMethod("setRootPath", java.lang.String.class);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			m.invoke(null, configPath);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}

		// 3.DAOConfiguration
		Class daoConfigClazz = null;
		try {
			daoConfigClazz = classLoader.loadClass("com.giantstone.base.config.DAOConfiguration");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}

		// setRootPath
		try {
			m = daoConfigClazz.getMethod("setRootPath", java.lang.String.class);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			m.invoke(null, configPath);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void createClassLoader() {
		// 1. root lib directory
		File rootLibDir = new File(libPath);
		checkLibDir(rootLibDir);

		List jarUrlList = new ArrayList(64);

		// 2. core lib
		File coreLibDir = new File(libPath + LIB_PATH_CORE);
		getAllJarUrl(jarUrlList, coreLibDir);

		// 3. external lib
		File extLibDir = new File(libPath + LIB_PATH_EXT);
		getAllJarUrl(jarUrlList, extLibDir);

		// 4. application lib 废弃，应用库都放到部署目录中
		// File appLibDir = new File(libPath + LIB_PATH_APPLICATION);
		// getAllJarUrl(jarUrlList, appLibDir);

		// 5. URLClassLoader
		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);
		for (int i = 0; i < urls.length; i++) {
			//System.out.println(urls[i]);
		}
		// **** edit by liugl 20090226
		// 通过线程传递ClassLoader
		// **old: AppendableURLClassloader cl = new
		// AppendableURLClassloader(urls, this
		// .getClass().getClassLoader());
		//AppendableURLClassloader cl = new AppendableURLClassloader(urls,
		//		Thread.currentThread().getContextClassLoader());
		
		//addJarFile(urls);
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		classLoader = cl;
		Thread.currentThread().setContextClassLoader(cl);

	}
	public 	void addJarFile(URL[] urls ) {
		ClassLoader preClassLoader = Thread.currentThread().getContextClassLoader();
		URLClassLoader ucl = URLClassLoader.newInstance(urls,preClassLoader);
		Thread.currentThread().setContextClassLoader(ucl);
	}

	private void getAllJarUrl(List jarUrlList, File libDir) {
		checkLibDir(libDir);
		String[] jarFiles = listAllJarFile(libDir);
		File jarFile = null;
		int i = 0;
		try {
			for (i = 0; i < jarFiles.length; i++) {
				jarFile = new File(libDir, jarFiles[i]);
				jarFile = jarFile.getCanonicalFile();
				jarUrlList.add(jarFile.toURL());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException("list jar[" + jarFiles[i] + "]", e);
			// throw new RuntimeException(MultiLanguageResourceBundle
			// .getInstance().getString("Bootstrap.listJar",
			// new String[] { jarFiles[i] }));
		}
	}

	private String[] listAllJarFile(File libDir) {
		return libDir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});
	}

	private void checkLibDir(File libDir) {
		// System.out.println(libDir);
		// System.out.println("exists="+libDir.exists());
		// System.out.println("isAbsolute="+libDir.isAbsolute());
		// System.out.println("isDirectory="+libDir.isDirectory());
		if (!libDir.exists()) {
			throw new IllegalArgumentException("libPath[" + libPath + "] doesn't exist!");
			// throw new IllegalArgumentException(MultiLanguageResourceBundle
			// .getInstance().getString("libPath.notExist",
			// new String[] { libPath }));
		}
		if (!libDir.isDirectory()) {
			throw new IllegalArgumentException("libPath[" + libPath + "] is not a directory!");
			// throw new IllegalArgumentException(MultiLanguageResourceBundle
			// .getInstance().getString("libPath.notDirectory",
			// new String[] { libPath }));
		}
		if (!libDir.canRead()) {
			throw new IllegalArgumentException("libPath[" + libPath + "] can not be read!");
			// throw new IllegalArgumentException(MultiLanguageResourceBundle
			// .getInstance().getString("libPath.canNotRead",
			// new String[] { libPath }));
		}
	}

	private static void printUsage() {
		System.out.println("Usage 1:");
		System.out.println("\tjava " + Bootstrap.class.getName() + " lib_path config_path deploy_path gateway_id {"
				+ CMD_START + "|" + CMD_STOP + "|" + CMD_RESET_LOGGER + "}");
		System.out.println("Usage 2:");
		System.out.println(
				"\tjava " + Bootstrap.class.getName() + " lib_path config_path deploy_path gateway_id DB_SUPPORT {"
						+ CMD_START + "|" + CMD_STOP + "|" + CMD_RESET_LOGGER + "}");
	}

	/**
	 * @return the libPath
	 */
	public String getLibPath() {
		return libPath;
	}

	/**
	 * @param libPath the libPath to set
	 */
	public void setLibPath(String libPath) {
		this.libPath = libPath.replace("\\", "/");
		if (!this.libPath.endsWith("/")) {
			this.libPath += "/";
		}
	}

	/**
	 * @return the configPath
	 */
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * @param configPath the configPath to set
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	/**
	 * @return the gatewayId
	 */
	public String getGatewayId() {
		return gatewayId;
	}

	/**
	 * @param gatewayId the gatewayId to set
	 */
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	/**
	 * @return the deployPath
	 */
	public String getDeployPath() {
		return deployPath;
	}

	/**
	 * @param deployPath the deployPath to set
	 */
	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

	public boolean isConfigDBSupport() {
		return configDBSupport;
	}

	public void setConfigDBSupport(boolean configDBSupport) {
		this.configDBSupport = configDBSupport;
	}

}
