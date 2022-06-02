package com.fib.autoconfigure.msgconverter.gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.cxf.helpers.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.msgconverter.channel.Channel;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelConfig;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelConfigParser;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelMainConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.Processor;
import com.fib.autoconfigure.msgconverter.channel.config.recognizer.RecognizerConfig;
import com.fib.autoconfigure.msgconverter.channel.event.EventHandler;
import com.fib.autoconfigure.msgconverter.channel.event.EventQueue;
import com.fib.autoconfigure.msgconverter.channel.event.impl.DefaultEventHandler;
import com.fib.autoconfigure.msgconverter.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.autoconfigure.msgconverter.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.autoconfigure.msgconverter.gateway.config.GatewayConfig;
import com.fib.autoconfigure.msgconverter.gateway.config.GatewayConfigParser;
import com.fib.autoconfigure.msgconverter.gateway.mapping.config.MappingRuleManager;
import com.fib.autoconfigure.msgconverter.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB.ChannelMode;
import com.fib.autoconfigure.msgconverter.message.metadata.Message;
import com.fib.autoconfigure.msgconverter.message.metadata.handler.MessageMetadataManager;
import com.fib.commons.classloader.CustomClassLoader;
import com.fib.commons.exception.CommonException;
import com.fib.commons.util.CommUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;

@Component
public class CommGateway {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommGateway.class);
	private String id;
	private String deployPath;

	/** 事件队列 */
	private EventQueue eventQueue;
	/** 网关配置 */
	private GatewayConfig config = new GatewayConfig();
	/**
	 * 通讯渠道列表
	 */
	private static Map<String, Channel> channels = new HashMap<>(64);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeployPath() {
		return deployPath;
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}

	public void start() {
		/* 1. 加载配置 */
		loadConfig();

		// 3. 加载模块
		loadModules();

		// 4. 加载通道
		loadChannels();

		// eventQueue = new EventQueue();

		// 7. 启动通道
		startChannels();
	}

	private void startChannels() {
		LOGGER.info("startChannels");
		Iterator<Channel> it = channels.values().iterator();
		Channel channel = null;
		while (it.hasNext()) {
			channel = it.next();
			// channel.setEventQueue(eventQueue);
			channel.start();
		}
	}

	private void loadChannels() {
		LOGGER.info("loadChannels");
		ChannelMainConfig channelMainConfig = null;
		Map channelSymbolMap = new HashMap();
		Iterator<ChannelMainConfig> it = config.getChannels().values().iterator();
		while (it.hasNext()) {
			channelMainConfig = it.next();
			if (channelMainConfig.isStartup()) {
				loadChannel(channelMainConfig);
			}
		}

		// 2. 加载所有通道配置后，检查所有的processor
		Channel channel = null;
		Iterator<Channel> iter = channels.values().iterator();
		while (iter.hasNext()) {
			channel = iter.next();
			ChannelConfig channelConfig = channel.getChannelConfig();
			checkChannelSymbolTable(channelConfig);
			checkChannelProcessors(channelConfig);
		}
	}

	private void checkChannelProcessors(ChannelConfig channelConfig) {
		String channelId = channelConfig.getMainConfig().getId();
		String messageGroupId = channelConfig.getMessageBeanGroupId();
		if (null == messageGroupId) {
			messageGroupId = channelId;
			channelConfig.setMessageBeanGroupId(messageGroupId);
		}

		Iterator<Processor> iterator = channelConfig.getProcessorTable().values().iterator();
		Processor processor = null;
//		MessageTransformerConfig messageTransformerConfig = null;
//		MessageHandlerConfig messageHandlerConfig = null;
		while (iterator.hasNext()) {
			processor = iterator.next();
			String processorId = processor.getId();
//			if ((!jobSupport)
//					&& (Processor.TYP_SAVE_AND_TRANSFORM == processor.getType() || Processor.TYP_SAVE_AND_TRANSMIT == processor.getType())) {
//				// throw new RuntimeException(
//				// "Gateway(Without Job) can not support processor type :"
//				// + Processor.getTextByType(processor.getType())
//				// + ", Channel Id["
//				// + channelConfig.getMainConfig().getId()
//				// + "], Processor Id[" + processor.getId() + "]");
//				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
//						"CommGateway.checkChannelProcessors.processorType.unsupport",
//						new String[] { Processor.getTextByType(processor.getType()), channelConfig.getMainConfig().getId(), processor.getId() }));
//			}

//			try {
//				// request-message-transformer
//				messageTransformerConfig = processor.getRequestMessageConfig();
//				if (null != messageTransformerConfig) {
//					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor, "request-message-transformer");
//				}
//
//				// request-message-handler
//				messageHandlerConfig = processor.getRequestMessageHandlerConfig();
//				if (messageHandlerConfig != null) {
//					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
//				}
//
//				// response-message-transformer
//				messageTransformerConfig = processor.getResponseMessageConfig();
//				if (null != messageTransformerConfig) {
//					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor, "response-message-transformer");
//				}
//
//				// response-message-heandler
//				messageHandlerConfig = processor.getResponseMessageHandlerConfig();
//				if (messageHandlerConfig != null) {
//					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
//				}
//
//				// error-message-transformer
//				messageTransformerConfig = processor.getErrorMessageConfig();
//				if (null != messageTransformerConfig) {
//					checkMessageTransformerConfig(messageTransformerConfig, messageGroupId, processor, "error-message-transformer");
//				}
//
//				// error-message-handler
//				messageHandlerConfig = processor.getErrorMessageHandlerConfig();
//				if (messageHandlerConfig != null) {
//					ClassUtil.createClassInstance(messageHandlerConfig.getClassName());
//				}
//				// error-mapping
//				ErrorMappingConfig errorMappingConfig = processor.getErrorMappingConfig();
//				if (null != errorMappingConfig) {
//					if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
//						if (!MessageMetadataManager.isMessageExist(messageGroupId, errorMappingConfig.getSoureMessageId())) {
//							// throw new RuntimeException(
//							// "error-mapping's @source-message-id is not exist!
//							// MessageGroup Id["
//							// + messageGroupId
//							// + "], Message Id["
//							// + errorMappingConfig
//							// .getSoureMessageId() + "]");
//							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
//									"CommGateway.checkChannelProcessors.errorMapping.sourceMessageId.notExist",
//									new String[] { messageGroupId, errorMappingConfig.getSoureMessageId() }));
//						}
//					}
//					// if (null == MappingRuleManager.getMappingRule(
//					// messageGroupId, errorMappingConfig
//					// .getMappingRuleId())) {
//					// throw new RuntimeException(
//					// "error-mapping's @bean-mapping["
//					// + errorMappingConfig.getMappingRuleId()
//					// + "] is not exist!");
//					// }
		}
//
//				// event
//				Iterator iter = processor.getEventConfig().values().iterator();
//				while (iter.hasNext()) {
//					List actionList = (List) iter.next();
//					ActionConfig actionConfig = null;
//					Iterator ita = actionList.iterator();
//					while (ita.hasNext()) {
//						actionConfig = (ActionConfig) ita.next();
//
//						if (ActionConfig.TYP_CLASS == actionConfig.getType()) {
//							try {
//								ClassUtil.createClassInstance(actionConfig.getClazz());
//							} catch (Exception e) {
//								// throw new RuntimeException("Action Class["
//								// + actionConfig.getClazz() + "]:"
//								// + e.getMessage(), e);
//								throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
//										"CommGateway.checkChannelProcessors.action.class.notExist",
//										new String[] { actionConfig.getClazz(), e.getMessage() }), e);
//							}
//						} else if (ActionConfig.TYP_JOB == actionConfig.getType()) {
//							if (!jobSupport) {
//								// throw new RuntimeException(
//								// "Gateway(without job) can not support "
//								// + "this action which type is \"job\", "
//								// + "Channel Id["
//								// + channelConfig.getMainConfig()
//								// .getId()
//								// + "], Processor Id["
//								// + processor.getId() + "]");
//								throw new RuntimeException(
//										MultiLanguageResourceBundle.getInstance().getString("CommGateway.checkChannelProcessors.action.unsupport",
//												new String[] { channelConfig.getMainConfig().getId(), processor.getId() }));
//							}
//							if (!channelConfig.getProcessorTable().containsKey(actionConfig.getProcessorRule().getProcessorId())) {
//								// throw new RuntimeException(
//								// "Can not find <action>'s processor["
//								// + actionConfig
//								// .getProcessorRule()
//								// .getProcessorId()
//								// + "] in Processor Table.");
//								throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
//										"CommGateway.checkChannelProcessors.action.processor.notFind",
//										new String[] { actionConfig.getProcessorRule().getProcessorId() }));
//							}
//						}
//					}
//				}

//			} catch (Exception e) {
//				//throw new CommonException("check channel[" + channelId + "]' processor[" + processorId + "] failed:" + e.getMessage(), e);

	}

	private void checkChannelSymbolTable(ChannelConfig channelConfig) {
		// TODO Auto-generated method stub

	}

	private void loadChannel(ChannelMainConfig channelMainConfig) {
		LOGGER.info("loadChannel...channelId={}", channelMainConfig.getId());
		// 1. channel deploy path
		String channelDeployPath = deployPath + channelMainConfig.getDeploy();
		if (!channelDeployPath.endsWith("/")) {
			channelDeployPath += "/";
		}
		if (!FileUtil.isDirectory(channelDeployPath)) {
			throw new CommonException(channelDeployPath + " is not directory");
		}
		ChannelConfig channelConfig = null;
		String channelConfigFileName = channelDeployPath + "channel.xml";
		ChannelConfigParser parser = new ChannelConfigParser();
		channelConfig = parser.parse(channelConfigFileName);

		LOGGER.info("loadChannel...channelId={},parse result is [{}]", channelMainConfig.getId(), channelConfig);
		channelConfig.setMainConfig(channelMainConfig);

		generateMessageBeanSourceFile(channelDeployPath, channelConfig);

		String relSrcRootPath = channelDeployPath + "src" + File.separatorChar;
		String srcRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {

		}

		List<String> srcFiles = new ArrayList<>();
		List<String> needMovedFiles = new ArrayList<>();

		// 查找待编译文件列表
		addSrcFilesToList(new File(srcRootPath), srcFiles);

		// 得到src、classes根路径
		String relClassRootPath = channelDeployPath + "classes" + File.separatorChar;
		String classRootPath = "";
		try {
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new CommonException("CommGateway.loadChannel.getCanonicalPath.failed", e);
		}
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
		if (!srcFiles.isEmpty()) {
			compileModifiedFiles(srcFiles, srcRootPath, classRootPath);
		}

		// 编译完成，把路径加到classLoader中
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();// this.getClass().getClassLoader();
		System.out.println(classLoader.getResource(""));
		if (classLoader instanceof CustomClassLoader custCl) {
			try {
				File classpathFile = new File(classRootPath);
				custCl.append(new URL[] { classpathFile.toURI().toURL() });
			} catch (MalformedURLException e) {
				throw new CommonException("CommGateway.loadChannel.getURL.failed", e);
			}

			ClassLoader currCl = this.getClass().getClassLoader();
			custCl.append(new URL[] {currCl.getResource("")});
		}
		// 3. load channel's library
		if (classLoader instanceof CustomClassLoader custCl) {
			String channelLibPath = channelDeployPath + "lib";
			if (FileUtil.exist(channelLibPath)) {
				try {
					custCl.append(channelLibPath);
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.loadLib.failed", e);
				}
			}
		}

		// 4. create channel instance
		Channel channel = null;
		Class<?> clazz = null;
		try {
			clazz = ClassLoaderUtil.loadClass(channelConfig.getClassName(), classLoader, true);
			channel = (Channel) ReflectUtil.newInstance(clazz);
		} catch (Exception e) {
			throw new CommonException("CommGateway.createChannelInstance.failed");
		}

		// set config
		channel.setMainConfig(channelMainConfig);
		channel.setChannelConfig(channelConfig);

		String connectorConfigFile = channelDeployPath + channelConfig.getConnectorConfig().getConfig();

		try (FileInputStream fin = new FileInputStream(FileUtil.file(connectorConfigFile))) {
			channel.loadConfig(fin);
		} catch (Exception e) {
			throw new CommonException("CommGateway.loadChannel.loadConnector.failed");
		}

		if (null != channelConfig.getMessageBeanGroupId() && !config.getChannels().containsKey(channelConfig.getMessageBeanGroupId())) {
			throw new CommonException("CommGateway.loadChannel.messageGroupId.notFound");
		}

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try {
			String className = "com.giantstone.cnaps2.messagebean.recv.req.CCMS_801_bean";
			String methodName = "validate";
			clazz = ClassLoaderUtil.loadClass(className, classloader, false);
			System.out.println(clazz.getClassLoader());
			Object obj = ReflectUtil.newInstance(clazz);

			Object rtn = ReflectUtil.invoke(obj, methodName);
			System.out.println(rtn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String mappingPath = channelDeployPath + "mapping";
		try {
			File mappingDir = FileUtil.file(mappingPath);
			if (mappingDir.exists()) {
				MappingRuleManager.loadMappingConfig(channel.getId(), mappingDir);
			}
		} catch (Exception e) {
			throw new CommonException("CommGateway.loadChannel.loadMapping.failed");
		}

		// 8. recognizer & eventHandler
		// 8.1. 非客户端（服务器、双向）通道必须有消息类型识别器，必须设置事件处理器
		if (ChannelMode.CLIENT != channelConfig.getMode()) {
			// messageTypeRecognizer
			if (null != channelConfig.getMessageTypeRecognizerConfig()) {
				try {
					channel.setMessageTypeRecognizer(
							createRecognizer(channelConfig.getMessageTypeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.createMessageTypeRecognizer.failed", e);
				}
			}

			// eventHandler
			if (null != channelConfig.getEventHandlerClass()) {
				try {
					channel.setEventHandler((EventHandler) ReflectUtil.newInstance(ClassLoaderUtil.loadClass(channelConfig.getEventHandlerClass())));
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.createEventHandler.failed");
				}
			} else {
				channel.setEventHandler(new DefaultEventHandler());
			}

		}
		// 8.2. 非服务器通（客户端、双向）道可能有返回码识别器
		if (ChannelMode.SERVER != channelConfig.getMode()) {
			// returnCodeRecognizer
			if (channelConfig.getReturnCodeRecognizerConfig() != null) {
				try {
					channel.setReturnCodeRecognizer(
							createRecognizer(channelConfig.getReturnCodeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.createRetCodeRecognizer.failed");
				}
			}

		}

		// 10. put to gateway's channel cache
		channels.put(channel.getId(), channel);

		ApplicationContext appCtx = SpringUtil.getApplicationContext();
		// DefaultListableBeanFactory defaultListableBeanFactory =
		// (DefaultListableBeanFactory) appCtx.getAutowireCapableBeanFactory();
		// BeanDefinitionBuilder beanDefinitionBuilder =
		// BeanDefinitionBuilder.genericBeanDefinition(clazz);

		// beanDefinitionBuilder.addPropertyValue("id", "mqChannel1");

		// defaultListableBeanFactory.registerBeanDefinition("mqChannel1",
		// beanDefinitionBuilder.getBeanDefinition());

		SpringUtil.registerBean(channel.getId(), channel);
		Channel c = appCtx.getBean(channel.getId(), Channel.class);
		System.out.println(c);

	}

	private AbstractMessageRecognizer createRecognizer(RecognizerConfig recognizerConfig, ClassLoader classLoader) {
		AbstractMessageRecognizer recognizer = null;
		Class<?> clazz = ClassLoaderUtil.loadClass(recognizerConfig.getClassName(), classLoader, true);
		recognizer = (AbstractMessageRecognizer) ReflectUtil.newInstance(clazz);
		recognizer.setParameters(recognizerConfig.getParameters());
		if (recognizer instanceof AbstractCompositeMessageRecognizer compositeRecognizer) {
			RecognizerConfig componentConfig = null;
			Iterator<RecognizerConfig> it = recognizerConfig.getComponentList().iterator();
			while (it.hasNext()) {
				componentConfig = it.next();
				compositeRecognizer.add(createRecognizer(componentConfig, classLoader));
			}
		}
		return recognizer;
	}

	private void compileModifiedFiles(List<String> modifiedFiles, String srcRootPath, String classRootPath) {
		File classRoot = FileUtil.file(classRootPath);
		FileUtils.mkDir(classRoot);
		// 取得所有的classloader的url并加到编译命令中
		ClassLoader appendableClassLoader = Thread.currentThread().getContextClassLoader();// this.getClass().getClassLoader();
//		URL[] urls = appendableClassLoader.getURLs();
//		StringBuffer sbUrls = new StringBuffer(1024);
//		for (URL url : urls) {
//			sbUrls.append(url.getPath()).append(System.getProperty("path.separator"));
//		}

		// JavaSourceCompiler.create(appendableClassLoader).addSource(FileUtil.file(srcRootPath)).compile();

//		Compiler compiler = ApplicationModel.defaultModel().getDefaultModule().getExtension(Compiler.class, "jdk");
//		for (String modifiedFile : modifiedFiles) {
////			Class<?> clazz = compiler.compile(FileUtil.readString(srcF, StandardCharsets.UTF_8), appendableClassLoader);
////
////			System.out.println(clazz.getClassLoader());
////			String className = clazz.getName();
////			System.out.println(className);
//			// 每次编译前判断该类是否已经编译过了(在其它类中有引用，则会提前编译)
//			String classFile = classRootPath + modifiedFile.substring(srcRootPath.length(), modifiedFile.length() - 5) + ".class";
//			if (new File(modifiedFile).lastModified() < new File(classFile).lastModified()) {
//				// 已经编译过，跳过
//				continue;
//			}
//		}

//		Iterator<String> iterator = modifiedFiles.iterator();
//		String modifiedFile = "";
//		String classFile = "";
//		while (iterator.hasNext()) {
//			modifiedFile = iterator.next();
//			classFile = classRootPath + modifiedFile.substring(srcRootPath.length(), modifiedFile.length() - 5) + ".class";
//			if (new File(modifiedFile).lastModified() < new File(classFile).lastModified()) {
//				// 已经编译过，跳过
//				iterator.remove();
//			}
//		}

		modifiedFiles.removeIf(modifiedFile -> {
			String classFile = classRootPath + modifiedFile.substring(srcRootPath.length(), modifiedFile.length() - 5) + ".class";
			return new File(modifiedFile).lastModified() < new File(classFile).lastModified();
		});

		String javaVersion = "17";
		String encoding = "UTF-8";
		CommUtils.compile(javaVersion, encoding, classRootPath, modifiedFiles);

//		List<URL> jarUrlList = new ArrayList<>(64);
//		jarUrlList.add(CommGateway.class.getClassLoader().getResource(""));
//		try {
//			jarUrlList.add(FileUtil.file(classRootPath).toURI().toURL());
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		}
//
//		URL[] urls = new URL[jarUrlList.size()];
//		jarUrlList.toArray(urls);
//
//		CustomClassLoader cl = new CustomClassLoader(urls, Thread.currentThread().getContextClassLoader());
//		Thread.currentThread().setContextClassLoader(cl);
	}

	public static void main(String[] args) {
		String javaVersion = "17";
		String encoding = "UTF-8";
		String srcRootPath = "F:/compile/src";
		String srcJava = "F:/compile/src/com/fib/cnaps/MsgHeader.java";
		String classRootPath = "F:/compile/classes";

		String classFile = classRootPath + srcJava.substring(srcRootPath.length(), srcJava.length() - 5) + ".class";
		if (new File(srcJava).lastModified() > new File(classFile).lastModified()) {
			// 已经编译过，跳过
			//
			CommUtils.compile(javaVersion, encoding, classRootPath, Arrays.asList(srcJava));
			System.out.println("编译");
		}

		List<URL> jarUrlList = new ArrayList<>(64);
		jarUrlList.add(CommGateway.class.getClassLoader().getResource(""));
		try {
			jarUrlList.add(FileUtil.file(classRootPath).toURI().toURL());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		URL[] urls = new URL[jarUrlList.size()];
		jarUrlList.toArray(urls);

		CustomClassLoader cl = new CustomClassLoader(urls, Thread.currentThread().getContextClassLoader());
		Thread.currentThread().setContextClassLoader(cl);

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try {
			String className = "com.fib.cnaps.messagebean.recv.req.MsgHeader";
			String methodName = "validate";
			Class<?> clazz = ClassLoaderUtil.loadClass(className, classloader, false);
			System.out.println(clazz.getClassLoader());
			Object obj = ReflectUtil.newInstance(clazz);

			Object rtn = ReflectUtil.invoke(obj, methodName, "fang");
			System.out.println(rtn);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找指定src路径中所有的源文件的全路径
	 * 
	 * @param srcRootPath   src的根目录
	 * @param modifiedFiles src文件列表
	 * @return src目录中所有的java文件全路径
	 */
	private void addSrcFilesToList(File srcRootPath, List<String> modifiedFiles) {
		if (!FileUtil.isDirectory(srcRootPath)) {
			return;
		}
		File[] files = srcRootPath.listFiles();
		for (File f : files) {
			// 是文件，加入列表
			if (f.isFile()) {
				if (f.getName().endsWith(".java")) {
					try {
						modifiedFiles.add(f.getCanonicalPath());
					} catch (IOException e) {
						throw new CommonException("CommGateway.loadChannel.getCanonicalPath.failed", e);
					}
				}
			} else {
				// 是路径，递归查找
				addSrcFilesToList(f, modifiedFiles);
			}
		}
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
			throw new CommonException("CommGateway.loadChannel.getCanonicalPath.failed");
		}

		File messageBeanDir = FileUtil.file(messageBeanPath);
		ChannelMainConfig channelMainConfig = channelConfig.getMainConfig();
		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);

		Message msg = null;
		String srcFilePath = "";
		String classFilePath = "";
		File srcFile = null;
		File classFile = null;

		if (!FileUtil.exist(messageBeanDir)) {
			return;
		}

		List<String> modifiedFiles = new ArrayList<>();
		MessageMetadataManager.loadMetadataGroup(channelMainConfig.getId(), messageBeanDir);

		Map<String, Message> allMsgInGroup = MessageMetadataManager.getAllMessage().get(channelMainConfig.getId());
		Iterator<Message> iter = allMsgInGroup.values().iterator();
		while (iter.hasNext()) {
			msg = iter.next();
			srcFilePath = srcRootPath + msg.getClassName().replace('.', File.separatorChar) + ".java";
			classFilePath = classRootPath + msg.getClassName().replace('.', File.separatorChar) + ".class";
			srcFile = new File(srcFilePath);
			classFile = new File(classFilePath);

			long configLastModifyTime = 0;

			configLastModifyTime = new File(messageBeanDir + File.separator + msg.getId() + ".xml").lastModified();
			if (!srcFile.exists() || !classFile.exists() || srcFile.lastModified() < configLastModifyTime) {
				// 配置文件有改变，生成java代码，加入列表
				mbcg.generate(msg, "UTF-8");
				modifiedFiles.add(srcFilePath);
			}
		}
	}

	private void loadModules() {
		// TODO Auto-generated method stub

	}

	private void loadConfig() {
		LOGGER.info("loadConfig");
		// 1. gateway config
		GatewayConfigParser parser = new GatewayConfigParser();
		config = parser.parse("outpro/config/gateway_" + id + ".xml");

		if (!FileUtil.isDirectory(deployPath)) {
			throw new CommonException(deployPath + " is not directory");
		}
	}
}