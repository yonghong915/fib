package com.fib.msgconverter.commgateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.config.ChannelSymbol;
import com.fib.msgconverter.commgateway.channel.config.database.ChannelConfigLoader;
import com.fib.msgconverter.commgateway.channel.config.processor.ErrorMappingConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageHandlerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageTransformerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ActionConfig;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.classloader.AppendableURLClassloader;
import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.config.GatewayConfig;
import com.fib.msgconverter.commgateway.config.GatewayConfigParser;
import com.fib.msgconverter.commgateway.config.ModuleConfig;
import com.fib.msgconverter.commgateway.config.database.GatewayConfigLoader;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLog;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLogDAO;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessage;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessageDAO;
import com.fib.msgconverter.commgateway.event.EventHandler;
import com.fib.msgconverter.commgateway.event.EventQueue;
import com.fib.msgconverter.commgateway.event.impl.DefaultEventHandler;
import com.fib.msgconverter.commgateway.mapping.config.MappingRuleManager;
import com.fib.msgconverter.commgateway.module.Module;
import com.fib.msgconverter.commgateway.module.constants.Constants;
import com.fib.msgconverter.commgateway.module.impl.MonitorModule;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.session.config.SessionConfig;
import com.fib.msgconverter.commgateway.util.EnumConstants;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.msgconverter.message.metadata.MessageMetadataManager;
import com.giantstone.base.config.DAOConfiguration;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.common.util.FileUtil;
import com.giantstone.message.dao.messagebean.dao.message.dao.MessageDAO;
import com.giantstone.message.metadata.Message;
import com.sun.tools.javac.Main;

/**
 * 通讯网关
 * 
 * @author 刘恭亮
 * 
 */
public class CommGateway {

	/**
	 * 网关id
	 */
	private static String id;

	/**
	 * 网关是否支持记录日志到数据库
	 */
	private static boolean databaseSupport = false;
	/**
	 * 网关是否支持异常处理监控
	 */
	private static boolean exceptionMonitorSupport = false;

	/**
	 * 网关配置文件是否存在数据库中
	 */
	private static boolean configDBSupport = false;

	/**
	 * 网关是否支持任务功能，如开启此功能则必须开启数据库功能
	 */
	private static boolean jobSupport = false;

	/**
	 * 网关配置
	 */
	private GatewayConfig config = new GatewayConfig();

	/**
	 * 通讯渠道列表
	 */
	private static Map<String,Channel> channels = new HashMap<>();

	/**
	 * 通道部署根路径
	 */
	private String deployPath;

	/**
	 * 事件队列
	 */
	private EventQueue eventQueue;

	/**
	 * 监控服务端口
	 */
	private ServerSocket server = null;

	/**
	 * 运行标识
	 */
	private boolean run = true;

	/**
	 * 日志记录器
	 */
	private static Logger logger;

	/**
	 * 20090113添加liuyi 编译java文件使用的编码方式
	 */
	private String encoding = "utf-8";

	/**
	 * 20090113添加liuyi 用于编译java文件的对象
	 */
	private static Main javac = new Main();

	/**
	 * 20090327添加wuhui 用于监控平台启动广播端口
	 */
	private static boolean isMonitorSupport = false;
	/**
	 * 用于存储信息摘要
	 */
	private static boolean isRecordSessionDigest = false;

	/**
	 * 变量元素表
	 */
	private static Properties variableConfig = null;

	public CommGateway(String id) {
		this.id = id;
	}

	public void start() {

		// 1. 加载配置
		loadConfig();

		variableConfig = config.getVariableConfig();

		// if (null != config.getVariableFile()) {
		// try {
		// variableConfig = new Properties(ConfigManager
		// .loadProperties(config.getVariableFile()));
		// } catch (Exception e) {
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "CommGateway.loadVariable.error",
		// new String[] { ExceptionUtil
		// .getExceptionDetail(e) }));
		// }
		// }

		// 2. log4j
		PropertyConfigurator.configure(ConfigManager.loadProperties("log4j_" + id + ".properties"));
		logger = Logger.getLogger(config.getLoggerName());

		try {
			// 设置DAO配置
			DAOConfiguration.configure("dao-config.properties");
			// 3. 加载模块
			// logger.error("---->> load modules");
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.start.loadModules"));
			loadModules();

			// 4. 加载通道
			// logger.error("---->> load channels");
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.start.loadChannels"));
			loadChannels();

			// 5. 启动事件处理器
			// logger.error("---->> start event queue");
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.start.startEventQueue"));
			eventQueue = new EventQueue();
			eventQueue.setLogger(logger);
			eventQueue.startDispatch(config.getEventHandlerNumber());

			// 6. 启动会话管理器
			if (config.getSessionConfig() != null) {
				SessionManager.setSessionConfig(config.getSessionConfig());
			} else {
				// 使用默认session设置：session超时5分钟
				SessionConfig sessionConfig = new SessionConfig();
				sessionConfig.setTimeout(300000);
				SessionManager.setSessionConfig(sessionConfig);
			}
			SessionManager.setLogger(logger);
			SessionManager.start();

			// 7. 启动通道
			// logger.error("---->> start channels");
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.start.startChannels"));
			try {
				startChannels();
			} catch (Exception e) {
				// logger.error("Start Channel Failed", e);
				stopChannels();
				ExceptionUtil.throwActualException(e);
			}

			// 8. 启动任务管理器
			// 做成组件管理，因此不在网关启动时开启
			// JobManager.loadAndScheduleJob();
			// CommGatewayConfigTest4DB.store2DB(config, channels);
			// System.out.println("store to db successfully");
			// 9. 启动监控
			startMonitor();
		} catch (Exception excp) {
			excp.printStackTrace();
			logger.error(excp);
			ExceptionUtil.throwActualException(excp);
		}
	}

	private void loadChannels() {

		// 1. 分别加载通道配置
		ChannelMainConfig channelMainConfig = null;
		Map channelSymbolMap = new HashMap();
		Iterator<ChannelMainConfig> it = config.getChannels().values().iterator();
		while (it.hasNext()) {
			channelMainConfig = it.next();
			if (channelMainConfig.isStartup()) {
				loadChannel(channelMainConfig);

				if (configDBSupport) {
					ChannelSymbol channelSymbol = new ChannelSymbol();
					channelSymbol.setChannlId(channelMainConfig.getId());
					channelSymbol.setName(channelMainConfig.getName());
					channelSymbol.setSymbol(channelMainConfig.getDatabaseChannelId() + "");
					channelSymbolMap.put(channelSymbol.getSymbol(), channelSymbol);
				}

			}
		}

		// 2. 加载所有通道配置后，检查所有的processor
		Channel channel = null;
		Iterator iter = channels.values().iterator();
		while (iter.hasNext()) {
			channel = (Channel) iter.next();
			ChannelConfig channelConfig = channel.getChannelConfig();
			if (configDBSupport) {
				channelConfig.setChannelSymbolTable(channelSymbolMap);
			}
			checkChannelSymbolTable(channelConfig);
			checkChannelProcessors(channelConfig);
		}

	}

	private void loadChannel(ChannelMainConfig channelMainConfig) {
		logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.loadChannel",
				new String[] { channelMainConfig.getId() }));

		// 1. channel deploy path
		String channelDeployPath = deployPath + channelMainConfig.getDeploy();
		if (!channelDeployPath.endsWith("/")) {
			channelDeployPath += "/";
		}
		File channelDeployDir = new File(channelDeployPath);
		if (!channelDeployDir.isDirectory()) {
			// throw new RuntimeException("channelDeployDir[" + channelDeployDir
			// + "] isn't a directory!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"CommGateway.loadChannel.deployPath.notDirectory", new String[] { "" + channelDeployDir }));
		}
		// --------------------20090213添加:编译所有src下面代码-------------------------
		// 得到src、classes根路径
		String relClassRootPath = channelDeployPath + "classes" + File.separatorChar;
		String channelConfigFileName = channelDeployPath + "channel.xml";
		String classRootPath = "";
		try {
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("CommGateway.loadChannel.getCanonicalPath.failed"), e);
		}

		// ===============20090212 liuyi添加 如果配置文件有改变则生成java文件
		// begin====================

		// ------------2009021320090213添加:编译所有src下面代码 over--------------------
		// 2. parse channel config file
		ChannelConfig channelConfig = null;
		if (configDBSupport) {
			ChannelConfigLoader loader = new ChannelConfigLoader();
			channelConfig = loader.loadConfig(channelMainConfig.getDatabaseChannelId());
		} else {
			channelConfigFileName = channelDeployPath + "channel.xml";
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(channelConfigFileName);
				ChannelConfigParser parser = new ChannelConfigParser();
				channelConfig = parser.parse(fin);
			} catch (Exception e) {
				// ExceptionUtil.throwActualException(e);
				// throw new RuntimeException("parse channel["
				// + channelMainConfig.getId() + "] config file "
				// + channelConfigFileName + " failed:" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannel.channelConfigFile.parse.failed",
						new String[] { channelMainConfig.getId(), channelConfigFileName, e.getMessage() }), e);
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}

		}
		channelConfig.setMainConfig(channelMainConfig);

		generateMessageBeanSourceFile(channelDeployPath, channelConfig);

		String relSrcRootPath = channelDeployPath + "src" + File.separatorChar;
		String srcRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("CommGateway.loadChannel.getCanonicalPath.failed"), e);
		}

		List<String> srcFiles = new ArrayList<String>();
		List<String> needMovedFiles = new ArrayList<String>();
		// 查找待编译文件列表
		addSrcFilesToList(new File(srcRootPath), srcFiles);
		// 去掉自上次编译后没有修改过的源码，以提高效率
		for (String srcFile : srcFiles) {
			String classFile = classRootPath + srcFile.substring(srcRootPath.length(), srcFile.length() - 5) + ".class";
			if (new File(srcFile).lastModified() < new File(classFile).lastModified()) {
				needMovedFiles.add(srcFile);
			}
		}

		for (String needMovedFile : needMovedFiles) {
			srcFiles.remove(needMovedFile);
		}

		// 编译文件列表
		if (srcFiles.size() > 0) {
			compileModifiedFiles(srcFiles, srcRootPath, classRootPath);
		}

		// 编译完成，把路径加到classLoader中
		// ClassLoader classLoader = this.getClass().getClassLoader();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader instanceof AppendableURLClassloader) {
			try {
				File classpathFile = new File(classRootPath);
				((AppendableURLClassloader) classLoader).append(new URL[] { classpathFile.toURL() });
			} catch (MalformedURLException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("CommGateway.loadChannel.getURL.failed", new String[] { classRootPath }), e);
			}
		}
		// 3. load channel's library
		if (classLoader instanceof AppendableURLClassloader) {
			String channelLibPath = channelDeployPath + "lib";
			if (FileUtil.isExist(channelLibPath)) {
				try {
					((AppendableURLClassloader) classLoader).append(channelLibPath);
				} catch (Exception e) {
					// throw new RuntimeException("load channel["
					// + channelMainConfig.getId() + "]'s library "
					// + channelLibPath + " failed:" + e.getMessage(), e);
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.loadChannel.loadLib.failed",
							new String[] { channelMainConfig.getId(), channelLibPath, e.getMessage() }), e);
				}
			}
		}
		// 4. create channel instance
		Class clazz = null;
		Channel channel = null;
		try {
			clazz = Class.forName(channelConfig.getClassName(), true, classLoader);
			channel = (Channel) clazz.newInstance();
		} catch (Exception e) {
			// ExceptionUtil.throwActualException(e);
			// throw new RuntimeException("create channel["
			// + channelMainConfig.getId() + "] instance failed:"
			// + e.getMessage(), e);
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("CommGateway.createChannelInstance.failed",
							new String[] { channelMainConfig.getId(), e.getMessage() }));
		}

		// set config
		channel.setMainConfig(channelMainConfig);
		channel.setChannelConfig(channelConfig);
		// 5. load connector config

		if (configDBSupport) {
			channel.loadConfig(null);
		} else {
			String connectorConfigFile = channelDeployPath + channelConfig.getConnectorConfig().getConfig();
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(connectorConfigFile);
				channel.loadConfig(fin);
			} catch (Exception e) {
				// ExceptionUtil.throwActualException(e);
				// throw new RuntimeException("load channel["
				// + channelMainConfig.getId() + "]'s connector config "
				// + connectorConfigFile + " failed:" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannel.loadConnector.failed",
						new String[] { channelMainConfig.getId(), connectorConfigFile, e.getMessage() }));
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException e) {
						// e.printStackTrace();
					}

				}
			}
		}
		if (null != channelConfig.getMessageBeanGroupId()) {

			if (!config.getChannels().containsKey(channelConfig.getMessageBeanGroupId())) {
				// throw new RuntimeException("Channel["
				// + channelMainConfig.getId()
				// + "]'s <message-bean>/@group-id ["
				// + channelConfig.getMessageBeanGroupId()
				// + "] can not be found!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannel.messageGroupId.notFound",
						new String[] { channelMainConfig.getId(), channelConfig.getMessageBeanGroupId() }));
			}
		}
		// 7. load mapping
		if (configDBSupport) {
			MappingRuleManager.loadMappingConfigByDB(channel.getId());
		} else {
			String mappingPath = channelDeployPath + "mapping";
			try {
				File mappingDir = new File(mappingPath);
				if (mappingDir.exists()) {
					MappingRuleManager.loadMappingConfig(channel.getId(), mappingDir);
				}
			} catch (Exception e) {
				// throw new RuntimeException("load channel["
				// + channelMainConfig.getId() + "]'s mapping config "
				// + mappingPath + " failed:" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannel.loadMapping.failed",
						new String[] { channelMainConfig.getId(), mappingPath, e.getMessage() }), e);
			}
		}
		// 8. recognizer & eventHandler
		// 8.1. 非客户端（服务器、双向）通道必须有消息类型识别器，必须设置事件处理器
		if (!(ChannelConfig.MODE_CLIENT == channelConfig.getMode())) {
			// messageTypeRecognizer
			if (null != channelConfig.getMessageTypeRecognizerConfig()) {
				try {
					channel.setMessageTypeRecognizer(createRecognizer(
							channelConfig.getMessageTypeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					// throw new RuntimeException("create channel["
					// + channelMainConfig.getId()
					// + "]'s messageTypeRecognizer failed:"
					// + e.getMessage(), e);
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.loadChannel.createMessageTypeRecognizer.failed",
							new String[] { channelMainConfig.getId(), e.getMessage() }), e);
				}
			}

			// eventHandler
			if (null != channelConfig.getEventHandlerClazz()) {
				try {
					channel.setEventHandler(
							(EventHandler) ClassUtil.createClassInstance(channelConfig.getEventHandlerClazz()));
				} catch (Exception e) {
					// throw new RuntimeException("create channel["
					// + channelMainConfig.getId() + "]'s EventHandler["
					// + channelConfig.getEventHandlerClazz()
					// + "] failed:" + e.getMessage(), e);
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("CommGateway.loadChannel.createEventHandler.failed", new String[] {
									channelMainConfig.getId(), channelConfig.getEventHandlerClazz(), e.getMessage() }),
							e);
				}
			} else {
				channel.setEventHandler(new DefaultEventHandler());
			}

		}
		// 8.2. 非服务器通（客户端、双向）道可能有返回码识别器
		if (!(ChannelConfig.MODE_SERVER == channelConfig.getMode())) {
			// returnCodeRecognizer
			if (channelConfig.getReturnCodeRecognizerConfig() != null) {
				try {
					channel.setReturnCodeRecognizer(createRecognizer(
							channelConfig.getReturnCodeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					// throw new RuntimeException("create channel["
					// + channelMainConfig.getId()
					// + "]'s returnCodeRecognizer failed:"
					// + e.getMessage(), e);
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.loadChannel.createRetCodeRecognizer.failed",
							new String[] { channelMainConfig.getId(), e.getMessage() }), e);
				}
			}

		}

		// 9. logger
		channel.getLogger();

		// 10. put to gateway's channel cache
		channels.put(channel.getId(), channel);

	}

	private void generateMessageBeanSourceFile(String channelDeployPath, ChannelConfig channelConfig) {
		String relSrcRootPath = channelDeployPath + "src" + File.separatorChar;
		String messageBeanPath = channelDeployPath + "message-bean";
		String relClassRootPath = channelDeployPath + "classes" + File.separatorChar;
		String srcRootPath = "";
		String classRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("CommGateway.loadChannel.getCanonicalPath.failed"), e);
		}

		File messageBeanDir = new File(messageBeanPath);
		ChannelMainConfig channelMainConfig = channelConfig.getMainConfig();
		// 取得所有的配置文件
		// File[] xmlFiles = messageBeanDir.listFiles(new FileFilter() {
		//
		// public boolean accept(File pathname) {
		// if (pathname.getName().endsWith(".xml")) {
		// return true;
		// }
		// return false;
		// }
		// });
		// 查看配置文件是否有改变，改变则生成源代码并编译，没有改变则不做任何事
		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);

		Message msg = null;
		String srcFilePath = "";
		String classFilePath = "";
		File srcFile = null;
		File classFile = null;

		if (messageBeanDir.exists()) {
			List<String> modifiedFiles = new ArrayList<String>();
			if (configDBSupport) {
				MessageMetadataManager.loadMetadataGroupByDB(channelMainConfig.getId());
			} else {
				if (messageBeanDir.exists()) {
					MessageMetadataManager.loadMetadataGroup(channelMainConfig.getId(), messageBeanDir);
				}
			}

			// for (File xmlFile : xmlFiles) {
			// 判断配置文件是否改变
			// msg = MessageMetadataManager.getMessage(channelMainConfig
			// .getId(), xmlFile.getName().substring(0,
			// xmlFile.getName().lastIndexOf(".xml")));
			Map allMsgInGroup = (Map) MessageMetadataManager.getAllMessage().get(channelMainConfig.getId());
			Iterator iter = allMsgInGroup.values().iterator();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			while (iter.hasNext()) {
				msg = (Message) iter.next();
				// if (null == msg) {
				// throw new RuntimeException(
				// MultiLanguageResourceBundle
				// .getInstance()
				// .getString(
				// "CommGateway.loadChannel.getMessage.failed",
				// new String[] {
				// channelMainConfig
				// .getId(),
				// xmlFile.getName() }));
				// }
				srcFilePath = srcRootPath + msg.getClassName().replace('.', File.separatorChar) + ".java";
				classFilePath = classRootPath + msg.getClassName().replace('.', File.separatorChar) + ".class";
				srcFile = new File(srcFilePath);
				classFile = new File(classFilePath);

				long configLastModifyTime = 0;
				if (configDBSupport) {
					Connection conn = null;
					try {
						conn = ConnectionManager.getInstance().getConnection();
						MessageDAO msgDao = new MessageDAO();
						msgDao.setConnection(conn);
						configLastModifyTime = simpleDateFormat
								.parse(msgDao.selectByPK(msg.getDatabaseMessageId()).getLastModifyTime()).getTime();
						conn.commit();
					} catch (Exception e) {
						if (null != conn) {
							try {
								conn.rollback();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					} finally {
						if (null != conn) {
							try {
								conn.close();
							} catch (Exception e) {
							}
						}
					}
				} else {
					configLastModifyTime = new File(messageBeanDir + File.separator + msg.getId() + ".xml")
							.lastModified();
				}

				if (!srcFile.exists() || !classFile.exists() || srcFile.lastModified() < configLastModifyTime) {
					// 配置文件有改变，生成java代码，加入列表
					mbcg.generate(msg, encoding);
					modifiedFiles.add(srcFilePath);
				}
			}
			// if (modifiedFiles.size() > 0) {
			// compileModifiedFiles(modifiedFiles, srcRootPath,
			// classRootPath);
			// }
		}

	}

	private void checkChannelSymbolTable(ChannelConfig channelConfig) {
		Map channelSymbolTable = channelConfig.getChannelSymbolTable();
		if (null == channelSymbolTable) {
			return;
		}
		Iterator iterator = channelSymbolTable.values().iterator();
		ChannelSymbol channelSymbol = null;
		while (iterator.hasNext()) {
			channelSymbol = (ChannelSymbol) iterator.next();
			if (!channels.containsKey(channelSymbol.getChannlId())) {
				// throw new RuntimeException("Unkown channel id["
				// + channelSymbol.getChannlId()
				// + "] in channel symbol table!Channel Id["
				// + channelConfig.getMainConfig().getId()
				// + "], channel symbol[" + channelSymbol.getSymbol()
				// + "]");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.loadChannle.channelId.unkown", new String[] { channelSymbol.getChannlId(),
								channelConfig.getMainConfig().getId(), channelSymbol.getSymbol() }));
			}
		}
	}

	/**
	 * 20090113liuyi添加 编译modifiedFiles中的所有源文件
	 * 
	 * @param modifiedFiles 文件路径的集合
	 * @param srcRootPath   源代码的跟路径
	 * @param classRootPath 类的跟路径
	 */
	private void compileModifiedFiles(List<String> modifiedFiles, String srcRootPath, String classRootPath) {
		File classRoot = new File(classRootPath);
		if (!classRoot.exists()) {
			classRoot.mkdir();
		}
		// 取得所有的classloader的url并加到编译命令中
		// URLClassLoader appendableClassLoader = (URLClassLoader)
		// this.getClass().getClassLoader();
		// URLClassLoader appendableClassLoader = (URLClassLoader)
		// Thread.currentThread().getContextClassLoader();

//		URL[] urls = appendableClassLoader.getURLs();
//		StringBuffer sbUrls = new StringBuffer(1024);
//		for (URL url : urls) {
//			sbUrls.append(url.getPath()).append(System.getProperty("path.separator"));
//		}
//		// E:\git_source\develop\fib\codes\fib-boot\fib-application\fib-msg-converter\target\classes
//		//sbUrls.append("/fib-application/fib-msg-converter/target/classes");
//		try {
//			Class clazz = appendableClassLoader.loadClass("com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle");
//			Constructor co =  clazz.getDeclaredConstructor();
//			co.setAccessible(true);
//			Object obj =  co.newInstance();
//		    System.out.println(obj);
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		URL[] sbUrls = null;
		try {
			sbUrls = new URL[] { new URL(
					"file:E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\src\\main\\resources\\lib"),
					new URL("file:E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\target\\classes") };
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arg = new String[] { "-encoding", encoding, "-d", classRootPath, "-sourcepath", srcRootPath, "-cp",
				sbUrls.toString(), "", "-g:none" };
		for (String modifiedFile : modifiedFiles) {
			if (null == modifiedFile || 0 == modifiedFile.length()) {
				continue;
			}
			// 每次编译前判断该类是否已经编译过了(在其它类中有引用，则会提前编译)
			String classFile = classRootPath + modifiedFile.substring(srcRootPath.length(), modifiedFile.length() - 5)
					+ ".class";
			if (new File(modifiedFile).lastModified() < new File(classFile).lastModified()) {
				// 已经编译过，跳过
				continue;
			}

			arg[8] = modifiedFile;
			System.out.println("compiling file : " + modifiedFile);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String code = cn.hutool.core.io.FileUtil.readUtf8String(modifiedFile);
//			ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
//			Compiler compiler = loader.getExtension("jdk");
//			Class<?> clazz = compiler.compile(code, classloader);

			String srcPath = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\outpro\\deploy\\bosent_cnaps2_recv\\src\\com\\giantstone\\cnaps2\\messagebean\\send\\req\\MsgHeader.java";
			srcPath = modifiedFile;
			String jars = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\target\\classes";
			String targetDir = classRootPath;
			String sourceDir = "E:/git_source/develop/fib/codes/fib-boot/fib-application/fib-msg-converter/outpro/deploy/bosent_cnaps2_recv/src";

			System.out.println("srcPath=" + srcPath);
			System.out.println("jars=" + jars);
			System.out.println("targetDir=" + targetDir);
			System.out.println("sourceDir=" + sourceDir);
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			List<File> sourceFileList = new ArrayList<>();
			sourceFileList.add(new File(srcPath));
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjectsFromFiles(sourceFileList);
			Iterable<String> options = Arrays.asList("-encoding", encoding, "-d", targetDir, "-sourcepath", sourceDir);

			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

			CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null,
					compilationUnits);
			Boolean result = compilationTask.call();
			// com.compile(code, classloader);
			if (result == null || !result) {
				// throw new RuntimeException(
				// "compile java file failed! error status:" + status
				// + ". source file:" + modifiedFile);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.compileModifiedFiles.compile.error", new String[] { "" + result, modifiedFile }));
			}

		}
	}

	private void checkChannelProcessors(ChannelConfig channelConfig) {
		String channelId = channelConfig.getMainConfig().getId();
		String messageGroupId = channelConfig.getMessageBeanGroupId();
		if (null == messageGroupId) {
			messageGroupId = channelId;
			channelConfig.setMessageBeanGroupId(messageGroupId);
		}

		Iterator iterator = channelConfig.getProcessorTable().values().iterator();
		Processor processor = null;
		MessageTransformerConfig messageTransformerConfig = null;
		MessageHandlerConfig messageHandlerConfig = null;
		while (iterator.hasNext()) {
			processor = (Processor) iterator.next();
			String processorId = processor.getId();
			if ((!jobSupport) && (EnumConstants.ProcessorType.SAVE_AND_TRANSFORM.getCode() == processor.getType()
					|| EnumConstants.ProcessorType.SAVE_AND_TRANSMIT.getCode() == processor.getType())) {
				// throw new RuntimeException(
				// "Gateway(Without Job) can not support processor type :"
				// + Processor.getTextByType(processor.getType())
				// + ", Channel Id["
				// + channelConfig.getMainConfig().getId()
				// + "], Processor Id[" + processor.getId() + "]");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.checkChannelProcessors.processorType.unsupport",
						new String[] { EnumConstants.ProcessorType.getNameByCode(processor.getType()),
								channelConfig.getMainConfig().getId(), processor.getId() }));
			}

			try {
				// request-message-transformer
				messageTransformerConfig = processor.getRequestMessageConfig();
				if (null != messageTransformerConfig) {
					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor,
							"request-message-transformer");
				}

				// request-message-handler
				messageHandlerConfig = processor.getRequestMessageHandlerConfig();
				if (messageHandlerConfig != null) {
					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
				}

				// response-message-transformer
				messageTransformerConfig = processor.getResponseMessageConfig();
				if (null != messageTransformerConfig) {
					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor,
							"response-message-transformer");
				}

				// response-message-heandler
				messageHandlerConfig = processor.getResponseMessageHandlerConfig();
				if (messageHandlerConfig != null) {
					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
				}

				// error-message-transformer
				messageTransformerConfig = processor.getErrorMessageConfig();
				if (null != messageTransformerConfig) {
					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor,
							"error-message-transformer");
				}

				// error-message-handler
				messageHandlerConfig = processor.getErrorMessageHandlerConfig();
				if (messageHandlerConfig != null) {
					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
				}
				// error-mapping
				ErrorMappingConfig errorMappingConfig = processor.getErrorMappingConfig();
				if (null != errorMappingConfig) {
					if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
							.getSourceChannelMessageObjectType()) {
						if (!MessageMetadataManager.isMessageExist(messageGroupId,
								errorMappingConfig.getSoureMessageId())) {
							// throw new RuntimeException(
							// "error-mapping's @source-message-id is not exist!
							// MessageGroup Id["
							// + messageGroupId
							// + "], Message Id["
							// + errorMappingConfig
							// .getSoureMessageId() + "]");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"CommGateway.checkChannelProcessors.errorMapping.sourceMessageId.notExist",
									new String[] { messageGroupId, errorMappingConfig.getSoureMessageId() }));
						}
					}
					// if (null == MappingRuleManager.getMappingRule(
					// messageGroupId, errorMappingConfig
					// .getMappingRuleId())) {
					// throw new RuntimeException(
					// "error-mapping's @bean-mapping["
					// + errorMappingConfig.getMappingRuleId()
					// + "] is not exist!");
					// }
				}

				// event
				Iterator iter = processor.getEventConfig().values().iterator();
				while (iter.hasNext()) {
					List actionList = (List) iter.next();
					ActionConfig actionConfig = null;
					Iterator ita = actionList.iterator();
					while (ita.hasNext()) {
						actionConfig = (ActionConfig) ita.next();

						if (ActionConfig.TYP_CLASS == actionConfig.getType()) {
							try {
								ClassUtil.createClassInstance(actionConfig.getClazz());
							} catch (Exception e) {
								// throw new RuntimeException("Action Class["
								// + actionConfig.getClazz() + "]:"
								// + e.getMessage(), e);
								throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
										"CommGateway.checkChannelProcessors.action.class.notExist",
										new String[] { actionConfig.getClazz(), e.getMessage() }), e);
							}
						} else if (ActionConfig.TYP_JOB == actionConfig.getType()) {
							if (!jobSupport) {
								// throw new RuntimeException(
								// "Gateway(without job) can not support "
								// + "this action which type is \"job\", "
								// + "Channel Id["
								// + channelConfig.getMainConfig()
								// .getId()
								// + "], Processor Id["
								// + processor.getId() + "]");
								throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
										"CommGateway.checkChannelProcessors.action.unsupport",
										new String[] { channelConfig.getMainConfig().getId(), processor.getId() }));
							}
							if (!channelConfig.getProcessorTable()
									.containsKey(actionConfig.getProcessorRule().getProcessorId())) {
								// throw new RuntimeException(
								// "Can not find <action>'s processor["
								// + actionConfig
								// .getProcessorRule()
								// .getProcessorId()
								// + "] in Processor Table.");
								throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
										"CommGateway.checkChannelProcessors.action.processor.notFind",
										new String[] { actionConfig.getProcessorRule().getProcessorId() }));
							}
						}
					}
				}

			} catch (Exception e) {
				// throw new RuntimeException("check channel[" + channelId
				// + "]' processor[" + processorId + "] failed:"
				// + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.checkChannelProcessors.check.failed",
						new String[] { channelId, processorId, e.getMessage() }), e);
			}
		}
	}

	private void checkMessageTransformerConfig(MessageTransformerConfig config, String groupId, Processor processor,
			String prefix) {
		if (EnumConstants.ProcessorType.TRANSMIT.getCode() == processor.getType()) {
			return;
		}

		String id = null;
		// source-message-id
		if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor.getSourceChannelMessageObjectType()) {
			id = config.getSourceMessageId();
			if (null != id && 0 < id.trim().length()) {
				if (!MessageMetadataManager.isMessageExist(groupId, id)) {
					// throw new RuntimeException(prefix
					// + " source-message-id is not exist!MessageGroupId["
					// + groupId + "], MessageId[" + id + "]");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.checkChannelProcessors.sourceMsgId.notExist",
							new String[] { prefix, groupId, id }));
				}
			}
		}

		// dest-message-id
		if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor.getDestChannelMessageObjectType()
				&& EnumConstants.ProcessorType.LOCAL.getCode() != processor.getType()) {
			id = config.getDestinationMessageId();
			if (null != id && 0 < id.trim().length()) {
				if (!MessageMetadataManager.isMessageExist(groupId, id)) {
					// throw new RuntimeException(prefix
					// + " dest-message-id is not exist! MessageGroupId["
					// + groupId + "], MessageId[" + id + "]");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.checkChannelProcessors.beanmapping.notExist", new String[] { prefix, id }));
				}
			}
		}
		// bean-mapping
		if (EnumConstants.ProcessorType.LOCAL.getCode() != processor.getType()) {
			id = config.getMappingId();
			if (null != id && 0 < id.trim().length()) {
				if (null == MappingRuleManager.getMappingRule(groupId, id)) {
					// throw new RuntimeException(prefix + " Mapping Rule[" + id
					// + "] is not exist!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"CommGateway.checkChannelProcessors.beanmapping.notExist", new String[] { prefix, id }));
				}
			}
		}
	}

	private AbstractMessageRecognizer createRecognizer(RecognizerConfig recognizerConfig, ClassLoader classLoader) {
		AbstractMessageRecognizer recognizer = null;
		try {
			Class clazz = Class.forName(recognizerConfig.getClassName(), true, classLoader);
			recognizer = (AbstractMessageRecognizer) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			ExceptionUtil.throwActualException(e);
		} catch (InstantiationException e) {
			ExceptionUtil.throwActualException(e);
		} catch (IllegalAccessException e) {
			ExceptionUtil.throwActualException(e);
		}
		recognizer.setParameters(recognizerConfig.getParameters());

		if (recognizer instanceof AbstractCompositeMessageRecognizer) {
			AbstractCompositeMessageRecognizer compositeRecognizer = (AbstractCompositeMessageRecognizer) recognizer;
			RecognizerConfig componentConfig = null;
			Iterator it = recognizerConfig.getComponentList().iterator();
			while (it.hasNext()) {
				componentConfig = (RecognizerConfig) it.next();
				compositeRecognizer.add(createRecognizer(componentConfig, classLoader));
			}
		}

		return recognizer;
	}

	/**
	 * 查找指定src路径中所有的源文件的全路径
	 * 
	 * @param srcRootPath   src的根目录
	 * @param modifiedFiles src文件列表
	 * @return src目录中所有的java文件全路径
	 */
	private void addSrcFilesToList(File srcRootPath, List<String> modifiedFiles) {

		if (srcRootPath.isDirectory()) {
			File[] files = srcRootPath.listFiles();
			for (File f : files) {
				// 是文件，加入列表
				if (f.isFile()) {
					if (f.getName().endsWith(".java")) {
						try {
							modifiedFiles.add(f.getCanonicalPath());
						} catch (IOException e) {
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
									.getString("CommGateway.loadChannel.getCanonicalPath.failed"), e);
						}
					}
				} else {
					// 是路径，递归查找
					addSrcFilesToList(f, modifiedFiles);
				}
			}
		}
	}

	private void loadModules() {
		if (null == config.getModules()) {
			return;
		}
		Iterator it = config.getModules().iterator();
		ModuleConfig moduleConfig = null;
		Module module = null;
		while (it.hasNext()) {
			moduleConfig = (ModuleConfig) it.next();
			// logger.error("---->>>> load module:" +
			// moduleConfig.getClassName());
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.loadModules.loadModule",
					new String[] { moduleConfig.getClassName() }));
			module = (Module) ClassUtil.createClassInstance(moduleConfig.getClassName());
			module.setParameter(moduleConfig.getParameters());
			module.initialize();
		}
	}

	private void stopModules() {
		Iterator it = config.getModules().iterator();
		ModuleConfig moduleConfig = null;
		Module module = null;
		while (it.hasNext()) {
			moduleConfig = (ModuleConfig) it.next();
			module = (Module) ClassUtil.createClassInstance(moduleConfig.getClassName());
			module.shutdown();
		}
	}

	private void startChannels() {
		Iterator it = channels.values().iterator();
		Channel channel = null;
		while (it.hasNext()) {
			channel = (Channel) it.next();
			channel.setEventQueue(eventQueue);
			// logger.error("---->>>> start channel:" + channel.getId());
			logger.error(MultiLanguageResourceBundle.getInstance().getString("CommGateway.startChannels.startChannel",
					new String[] { channel.getId() }));
			channel.start();
		}
	}

	public static final String CMD_SEPERATOR_REALVALUE = "|";

	public static final String CMD_START = "start";
	public static final String CMD_STOP = "stop";
	public static final String CMD_RESET_LOGGER = "reset_logger";
	// 监控平台发起的启动监控命令 addby wuhui 20090320
	public static final String CMD_MONITOR_LOGIN = "login";
	// 监控平台发起停止监控命令 add by wangjing 20100309
	public static final String CMD_STOP_MONITOR = "stop_monitor";
	// 通讯请求消息
	public static final String COMMUICATE_CHECK_MSG_REQ = "check";
	// 通讯回应消息
	public static final String COMMUICATE_CHECK_MSG_RES = "check_right";
	// private static String JDBC_CON_FILE = "connection-manager.xml";
	public static final String CMD_SEARCH = "search";
	// 重发
	public static final String CMD_RESEND_PREFIX = "resend";
	public static final String CMD_RESEND_4_SESSIONID = "resend";
	public static final String CMD_RESEND_4_EXTERNALSERIALNUM = "resend4externalSerialNum";
	// 开启/关闭屏蔽敏感信息开关
	public static final String CMD_START_FILTER = "start_filter";
	public static final String CMD_STOP_FILTER = "stop_filter";
	// 查询当前活动的处理线程数量
	public static final String CMD_SEARCH_ALIVE_HANDLER = "search_alive_event_handler_num";
	// 重置处理线程池
	public static final String CMD_RESET_HANDLER_POOL = "reset_event_handler_pool";
	// 启动会话摘要记录模块
	public static final String CMD_START_RECORD_DIGEST = "start_record_digest";
	// 停止会话摘要记录模块
	public static final String CMD_STOP_RECORD_DIGEST = "stop_record_digest";

	// add finished
	private void startMonitor() {

		try {
			server = new ServerSocket(config.getMonitorPort(), 5);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		Socket s = null;
		InputStream in = null;
		OutputStream out = null;
		byte[] req = new byte[1024];
		String command = null;
		byte[] ret = null;
		while (run) {

			try {
				s = server.accept();
			} catch (IOException e) {
				// e.printStackTrace();
				if (!server.isClosed()) {
					continue;
				}
			}

			try {
				s.setSoTimeout(5000);
				in = s.getInputStream();
				int onceRead = in.read(req);
				if (-1 == onceRead) {
					// throw new RuntimeException(
					// "onceRead=-1 ! remote connection maybe closed");
					// throw new RuntimeException(MultiLanguageResourceBundle
					// .getInstance().getString("onceRead.-1"));
					continue;
				}
				command = new String(req, 0, onceRead);
				if (logger.isDebugEnabled()) {
					logger.debug(MultiLanguageResourceBundle.getInstance().getString("CommGateway.startMonitor.command",
							new String[] { command }));
				}
				ret = "success".getBytes();
				if (CMD_STOP.equals(command)) {
					try {
						stopChannels();
						stopModules();
						SessionManager.stop();
					} catch (Exception ex) {
						ret = ex.getMessage().getBytes();
					}
				} else if (CMD_RESET_LOGGER.equals(command)) {
					resetLogger();
				} else if (CMD_START_RECORD_DIGEST.equals(command)) {
					CommGateway.setRecordSessionDigest(true);
				} else if (CMD_STOP_RECORD_DIGEST.equals(command)) {
					CommGateway.setRecordSessionDigest(false);
				} else if (null != command && command.startsWith(CMD_MONITOR_LOGIN)) {
					if (!isMonitorSupport) {
						ByteBuffer buf = new ByteBuffer();
						buf.append(Constants.UNSUPPORT_MONITOR);
						buf.append(CodeUtil.IntToBytes(0));
						byte[] msgBody = buf.toBytes();
						byte[] msg = new byte[msgBody.length + 4];
						System.arraycopy(CodeUtil.IntToBytes(msgBody.length), 0, msg, 0, 4);
						System.arraycopy(msgBody, 0, msg, 4, msgBody.length);
						ret = msg;
					} else {
						ret = MonitorModule
								.checkMonitorLogin(command.substring(command.indexOf(CMD_SEPERATOR_REALVALUE) + 1));
					}
				} else if (COMMUICATE_CHECK_MSG_REQ.equals(command)) {
					ret = COMMUICATE_CHECK_MSG_RES.getBytes();
					// } else if (CMD_SEARCH.equals(command)) {
					// String msg = new String(req, 30, onceRead - 30);
					// String result = searchProcessor(msg);
					// if (null != result) {
					// ret = result.getBytes();
					// }
				} else if (null != command && command.startsWith(CMD_RESEND_PREFIX)) {
					if (logger.isInfoEnabled()) {
						logger.info("command is " + command);
					}
					try {
						ret = resend(command).getBytes();
					} catch (Exception exce) {
						ret = exce.getMessage().getBytes();
					}
				} else if (CMD_START_FILTER.equals(command)) {
					CommGateway.setShieldSensitiveFields(true);
					ret = "success".getBytes();
				} else if (CMD_STOP_FILTER.equals(command)) {
					CommGateway.setShieldSensitiveFields(false);
					ret = "success".getBytes();
				} else if (CMD_SEARCH_ALIVE_HANDLER.equals(command)) {
					ret = ("" + eventQueue.getAliveEventHandlerNum()).getBytes();
				} else if (CMD_RESET_HANDLER_POOL.equals(command)) {
					try {
						eventQueue.resetEventHandlerPool();
						ret = "success".getBytes();
					} catch (Exception e) {
						ret = ExceptionUtil.getExceptionDetail(e).getBytes();
					}
				} else {// add finished
					ret = "wrong command!".getBytes();
				}

				out = s.getOutputStream();
				// System.out.println("\nret="+ret);

				out.write(ret);

				if (CMD_STOP.equals(command)) {
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				continue;
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
		try {
			if (null != server && !server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
		System.exit(0);

	}

	private String resend(String command) {
		if (-1 == command.indexOf(CMD_SEPERATOR_REALVALUE)) {
			return "wrong command! " + CMD_RESEND_4_SESSIONID + "|sessionId  or " + CMD_RESEND_4_EXTERNALSERIALNUM
					+ "|externalSerialNum!";
		}
		String commandTitle = command.split(CMD_SEPERATOR)[0].trim();
		String id = command.split(CMD_SEPERATOR)[1].trim();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			CommLogDAO commLogDAO = new CommLogDAO(false, conn);
			CommLog commLog = null;
			if (CMD_RESEND_4_SESSIONID.equalsIgnoreCase(commandTitle)) {
				commLog = commLogDAO.selectByPK(id);
				if (null == commLog) {
					return "wrong sessionId";
				}
			} else {
				return "wrong command";
				// commLog = commLogDAO.selectByExternalSerialNumber(id);
				// if (null == commLog) {
				// return "wrong externalSerialNum";
				// }
			}

			Channel destChannel = (Channel) channels.get(commLog.getDestChannelId());
			Channel sourceChannel = (Channel) channels.get(commLog.getSourceChannelId());
			Processor processor = (Processor) destChannel.getChannelConfig().getProcessorTable()
					.get(commLog.getProcessorId());
			if (null == processor) {
				processor = (Processor) sourceChannel.getChannelConfig().getProcessorTable()
						.get(commLog.getProcessorId());
			}
			CommLogMessageDAO commLogMessageDAO = new CommLogMessageDAO(false, conn);
			CommLogMessage commLogMessage = commLogMessageDAO.selectByPK(commLog.getId(), SessionConstants.DST_REQ);

			Processor resendProcessor = new Processor();
			resendProcessor.setId(processor.getId());
			resendProcessor.setSourceAsync(true);
			resendProcessor.setDestAsync(processor.isDestAsync());

			Session resendSession = SessionManager.createSession();
			// resendSession.setExternalSerialNum(commLog.getExternalSerialNum())
			// ;
			resendSession.setType(Session.TYP_EXTERNAL);
			resendSession.setSourceChannel(sourceChannel);
			resendSession.setProcessor(resendProcessor);
			resendSession.setDestChannel(destChannel);
			resendSession.setDestRequestMessage(commLogMessage.getMessage());
			SessionManager.attachSession(commLogMessage.getMessage(), resendSession);

			destChannel.sendRequestMessage(commLogMessage.getMessage(), processor.isDestAsync(),
					processor.getTimeout());

			conn.commit();
			return resendSession.getId();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getExceptionDetail(e));
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}

			return e.getMessage();
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

	}

	private static final String CMD_SEPERATOR = "\\|";

	private void stopChannels() {
		// ChannelManager.stopAllChannels();
		run = false;

	}

	private void resetLogger() {
		PropertyConfigurator.configure(ConfigManager.loadProperties("log4j_" + id + ".properties"));

	}

	private void loadConfig() {
		if (configDBSupport) {
			GatewayConfigLoader loader = new GatewayConfigLoader();
			config = loader.loadConfig(id);
		} else {
			// 1. gateway config
			GatewayConfigParser parser = new GatewayConfigParser();
			config = parser.parse("gateway_" + id + ".xml");
			// 2. deploy path
			File deployDir = new File(deployPath);
			if (!deployDir.isDirectory()) {
				// throw new RuntimeException("deployPath[" + deployDir
				// + "] isn't a directory!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("CommGateway.loadConfig.deployPath.notDirectory", new String[] { "" + deployDir }));
			}
		}

	}

	public static void main(String[] args) {
		if (!(args.length == 1 || args.length == 3 || args.length == 5)) {
			printUsage();
			System.exit(-1);
		}
		if (args.length == 3) {
			if (!("-c".equals(args[0]) || "-d".equals(args[0]))) {
				printUsage();
				System.exit(0);
			}
		}
		if (args.length == 5) {
			if (!("-c".equals(args[0]) && "-d".equals(args[2]))) {
				printUsage();
				System.exit(0);
			}
		}

		String id = null;
		String configPath = "config";
		String deployPath = "deploy";
		if (args.length == 3) {
			if ("-c".equals(args[0])) {
				configPath = args[1];
			} else {
				deployPath = args[1];
			}
			id = args[2];
		} else if (args.length == 5) {
			configPath = args[1];
			deployPath = args[3];
			id = args[4];
		} else {
			id = args[0];
		}

		ConfigManager.setRootPath(configPath);
		DAOConfiguration.setRootPath(configPath);
		CommGateway gw = new CommGateway(id);

		gw.setDeployPath(deployPath);
		gw.start();

	}

	private boolean isExists(String id, Map result) {
		if (null == result) {
			return true;
		}
		if (null != result.get(id)) {
			return true;
		}
		return false;
	}

	private static void printUsage() {
		System.out.println("Usage :");
		System.out.println("\t" + CommGateway.class.getName() + "[-c configPath] [-d deployPath] gateway_id");
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
		if (!this.deployPath.endsWith("/")) {
			this.deployPath += "/";
		}
	}

	public static Map getChannels() {
		return channels;
	}

	public static void setChannels(Map channels) {
		CommGateway.channels = channels;
	}

	public static boolean isJobSupport() {
		return jobSupport;
	}

	public static void setJobSupport(boolean jobSupport) {
		CommGateway.jobSupport = jobSupport;
	}

	public static boolean isDatabaseSupport() {
		return databaseSupport;
	}

	public static void setDatabaseSupport(boolean databaseSupport) {
		CommGateway.databaseSupport = databaseSupport;
	}

	public static boolean isExceptionMonitorSupport() {
		return exceptionMonitorSupport;
	}

	public static void setExceptionMonitorSupport(boolean exceptionMonitorSupport) {
		CommGateway.exceptionMonitorSupport = exceptionMonitorSupport;
	}

	public static boolean isMonitorSupport() {
		return isMonitorSupport;
	}

	public static void setMonitorSupport(boolean isMonitorSupport) {
		CommGateway.isMonitorSupport = isMonitorSupport;
	}

	public static boolean isRecordSessionDigest() {
		return isRecordSessionDigest;
	}

	public static void setRecordSessionDigest(boolean isRecordSessionDigest) {
		CommGateway.isRecordSessionDigest = isRecordSessionDigest;
	}

	public static Properties getVariableConfig() {
		return variableConfig;
	}

	public static void setVariableConfig(Properties variableConfig) {
		CommGateway.variableConfig = variableConfig;
	}

	public static boolean isConfigDBSupport() {
		return configDBSupport;
	}

	public static void setConfigDBSupport(boolean configDBSupport) {
		CommGateway.configDBSupport = configDBSupport;
	}

	public static Logger getMainLogger() {
		return logger;
	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		CommGateway.id = id;
	}

	public static boolean isShieldSensitiveFields() {
		return SessionManager.getSessionConfig().isShieldSensitiveField();
	}

	private static void setShieldSensitiveFields(boolean isShield) {
		SessionManager.getSessionConfig().setShieldSensitiveField(isShield);
	}

	public static String getSensitiveFields() {
		return SessionManager.getSessionConfig().getSensitiveFieldsInMap();
	}

	public static char getSensitiveReplaceChar() {
		return SessionManager.getSessionConfig().getSensitiveFieldsRelpace();
	}
}
