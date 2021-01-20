package com.fib.gateway;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.ClassUtil;
import com.fib.commons.util.ClasspathUtil;
import com.fib.gateway.channel.config.ChannelConfig;
import com.fib.gateway.channel.config.ChannelConfigParser;
import com.fib.gateway.channel.config.ChannelSymbol;
import com.fib.gateway.channel.config.processor.ErrorMappingConfig;
import com.fib.gateway.channel.config.processor.MessageHandlerConfig;
import com.fib.gateway.channel.config.processor.MessageTransformerConfig;
import com.fib.gateway.channel.config.processor.event.ActionConfig;
import com.fib.gateway.channel.config.recognizer.RecognizerConfig;
import com.fib.gateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.gateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.gateway.classloader.AppendableURLClassloader;
import com.fib.gateway.config.ChannelMainConfig;
import com.fib.gateway.config.GatewayConfig;
import com.fib.gateway.config.GatewayConfigParser;
import com.fib.gateway.config.ModuleConfig;
import com.fib.gateway.mapping.config.MappingRuleManager;
import com.fib.gateway.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.message.xml.config.processor.Processor;
import com.fib.gateway.message.xml.event.DefaultEventHandler;
import com.fib.gateway.message.xml.event.EventHandler;
import com.fib.gateway.message.xml.event.EventQueue;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.module.Module;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-05
 */
@Data
public class CommGateway {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

	private String id;
	private String deployPath;
	private GatewayConfig config = new GatewayConfig();
	private EventQueue eventQueue;
	private static Map<String, Channel> channels = new HashMap<>();
	private static boolean jobSupport = false;
	public static Map getChannels() {
		return channels;
	}
	public List<String> generateSourceFile(String deployPath, ChannelConfig channelConfig) {
		logger.info("generateSourceFile->generate source file,deploy path:{}", deployPath);
		String relSrcRootPath = deployPath + "src" + File.separatorChar;
		String messageBeanPath = deployPath + "message-bean";
		String relClassRootPath = deployPath + "classes";
		String srcRootPath = "";
		String classRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new CommonException("aaaa");
		}

		File messageBeanDir = new File(messageBeanPath);
		if (!messageBeanDir.exists()) {
			logger.warn("path {} not exists.", messageBeanPath);
			return Collections.emptyList();
		}

		ChannelMainConfig channelMainConfig = channelConfig.getMainConfig();
		String channelId = channelMainConfig.getId();

		MessageMetadataManager.loadMetadataGroup(channelId, messageBeanDir);

		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);
		Map<String, Message> allMsgInGroup = MessageMetadataManager.getAllMessage().get(channelId);
		Iterator<Message> iter = allMsgInGroup.values().iterator();
		Message msg = null;
		String srcFilePath = "";
		String classFilePath = "";
		File srcFile = null;
		File classFile = null;
		List<String> modifiedFiles = new ArrayList<>();
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
				mbcg.generate(msg, DEFAULT_ENCODING);
				modifiedFiles.add(srcFilePath);
			}
		}
		return modifiedFiles;
	}

	public void start() {
		logger.info("commGateway....");
		loadConfig();

		loadModules();

		loadChannels();

		logger.info("CommGateway.start.startEventQueue");
		eventQueue = new EventQueue();
		// eventQueue.setLogger(logger);
		eventQueue.startDispatch(config.getEventHandlerNumber());

		logger.info("CommGateway.start.startChannels");
		startChannels();
	}

	private void loadModules() {
		if (null == config.getModules()) {
			return;
		}
		logger.info("loadModules start... ");
		Iterator<ModuleConfig> it = config.getModules().iterator();
		ModuleConfig moduleConfig = null;
		Module module = null;
		while (it.hasNext()) {
			moduleConfig = it.next();
			logger.info("CommGateway.loadModules.loadModule");
			Object obj = ClassUtil.createClassInstance(moduleConfig.getClassName());
			if (obj instanceof Module) {
				module = (Module) obj;
				module.setParameter(moduleConfig.getParameters());
				module.initialize();
			}
		}
		logger.info("loadModules end... ");
	}

	private void startChannels() {
		logger.info("startChannels... ");
		Iterator<Channel> it = channels.values().iterator();
		Channel channel = null;
		while (it.hasNext()) {
			channel = it.next();
			channel.setEventQueue(eventQueue);
			logger.info("CommGateway.startChannels.startChannel");
			channel.start();
		}
	}

	private void loadChannels() {
		logger.info("loadChannels start... ");
		ChannelMainConfig channelMainConfig = null;
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
		logger.info("loadChannels end... ");
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
				throw new RuntimeException("CommGateway.loadChannle.channelId.unkown");
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
			if ((!jobSupport) && (Processor.TYP_SAVE_AND_TRANSFORM == processor.getType()
					|| Processor.TYP_SAVE_AND_TRANSMIT == processor.getType())) {
				// throw new RuntimeException(
				// "Gateway(Without Job) can not support processor type :"
				// + Processor.getTextByType(processor.getType())
				// + ", Channel Id["
				// + channelConfig.getMainConfig().getId()
				// + "], Processor Id[" + processor.getId() + "]");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"CommGateway.checkChannelProcessors.processorType.unsupport",
						new String[] { Processor.getTextByType(processor.getType()),
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
					if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
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
								throw new RuntimeException(
										"CommGateway.checkChannelProcessors.action.processor.notFind");
							}
						}
					}
				}

			} catch (Exception e) {
				// throw new RuntimeException("check channel[" + channelId
				// + "]' processor[" + processorId + "] failed:"
				// + e.getMessage(), e);
				throw new RuntimeException("CommGateway.checkChannelProcessors.check.failed");
			}
		}
	}

	private void checkMessageTransformerConfig(MessageTransformerConfig config, String groupId, Processor processor,
			String prefix) {
		if (Processor.TYP_TRANSMIT == processor.getType()) {
			return;
		}

		String id = null;
		// source-message-id
		if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
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
		if (Processor.MSG_OBJ_TYP_MB == processor.getDestChannelMessageObjectType()
				&& Processor.TYP_LOCAL != processor.getType()) {
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
		if (Processor.TYP_LOCAL != processor.getType()) {
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

	private void loadChannel(ChannelMainConfig channelMainConfig) {
		logger.info("CommGateway.loadChannel,channelId={}", channelMainConfig.getId());
		String channelDeployPath = deployPath + channelMainConfig.getDeploy();
		if (!channelDeployPath.endsWith("/")) {
			channelDeployPath += "/";
		}
		File channelDeployDir = new File(channelDeployPath);
		if (!channelDeployDir.isDirectory()) {
			throw new CommonException("CommGateway.loadChannel.deployPath.notDirectory," + channelDeployDir);
		}
		String relSrcRootPath = channelDeployPath + "src" + File.separatorChar;
		String relClassRootPath = channelDeployPath + "classes" + File.separatorChar;
		String classRootPath = "";
		String srcRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new CommonException("CommGateway.loadChannel.getCanonicalPath.failed", e);
		}
		ChannelConfig channelConfig = null;
		String channelConfigFileName = channelDeployPath + "channel.xml";

		try (FileInputStream fin = new FileInputStream(channelConfigFileName);) {
			ChannelConfigParser parser = new ChannelConfigParser();
			channelConfig = parser.parse(fin);
		} catch (Exception e) {
			logger.error("aaa", e);
			throw new CommonException("aaa");
		}
		channelConfig.setMainConfig(channelMainConfig);
		List<String> modifiedFiles = generateSourceFile(channelDeployPath, channelConfig);
		if (CollUtil.isNotEmpty(modifiedFiles)) {
			ClasspathUtil.compileFiles(modifiedFiles, srcRootPath, classRootPath, "UTF-8");
		}

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader instanceof AppendableURLClassloader) {
			File classpathFile = new File(classRootPath);
			try {
				((AppendableURLClassloader) classLoader).append(new URL[] { classpathFile.toURL() });
			} catch (MalformedURLException e) {
			}
		}

		List<File> classList = FileUtil.loopFiles(classRootPath, (File dir) -> dir.getName().endsWith(".class"));
		System.out.println(classList.size());

		Class<?> clazz = null;
		Channel channel = null;
		try {
			clazz = Class.forName(channelConfig.getClassName(), true, classLoader);
			Object obj = clazz.getDeclaredConstructor().newInstance();
			if (obj instanceof Channel) {
				channel = (Channel) obj;
			}
		} catch (Exception e) {
			throw new CommonException("CommGateway.createChannelInstance.failed");
		}
		if (Objects.isNull(channel)) {
			throw new CommonException("channel is null");
		}
		channel.setMainConfig(channelMainConfig);
		channel.setChannelConfig(channelConfig);

		String connectorConfigFile = channelDeployPath + channelConfig.getConnectorConfig().getConfig();
		try (FileInputStream fin = new FileInputStream(connectorConfigFile);) {
			channel.loadConfig(fin);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != channelConfig.getMessageBeanGroupId()) {
			if (!config.getChannels().containsKey(channelConfig.getMessageBeanGroupId())) {
				throw new CommonException("CommGateway.loadChannel.messageGroupId.notFound");
			}
		}

		String mappingPath = channelDeployPath + "mapping";
		try {
			File mappingDir = new File(mappingPath);
			if (mappingDir.exists()) {
				MappingRuleManager.loadMappingConfig(channel.getId(), mappingDir);
			}
		} catch (Exception e) {
			throw new CommonException("CommGateway.loadChannel.loadMapping.failed");
		}

		// 8. recognizer & eventHandler
		// 8.1. 非客户端（服务器、双向）通道必须有消息类型识别器，必须设置事件处理器
		if (ChannelConfig.MODE_CLIENT != channelConfig.getMode()) {
			// messageTypeRecognizer
			if (null != channelConfig.getMessageTypeRecognizerConfig()) {
				try {
					channel.setMessageTypeRecognizer(createRecognizer(
							channelConfig.getMessageTypeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					e.printStackTrace();
					throw new CommonException("CommGateway.loadChannel.createMessageTypeRecognizer.failed");
				}
			}

			// eventHandler
			if (null != channelConfig.getEventHandlerClazz()) {
				try {
					channel.setEventHandler(
							(EventHandler) ClassUtil.createClassInstance(channelConfig.getEventHandlerClazz()));
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.createEventHandler.failed");
				}
			} else {
				channel.setEventHandler(new DefaultEventHandler());
			}

		}
		// 8.2. 非服务器通（客户端、双向）道可能有返回码识别器
		if (ChannelConfig.MODE_SERVER != channelConfig.getMode()) {
			// returnCodeRecognizer
			if (channelConfig.getReturnCodeRecognizerConfig() != null) {
				try {
					channel.setReturnCodeRecognizer(createRecognizer(
							channelConfig.getReturnCodeRecognizerConfig().getRecognizerConfig(), classLoader));
				} catch (Exception e) {
					throw new CommonException("CommGateway.loadChannel.createRetCodeRecognizer.failed");
				}
			}

		}
		// 10. put to gateway's channel cache
		channels.put(channel.getId(), channel);
	}

	private AbstractMessageRecognizer createRecognizer(RecognizerConfig recognizerConfig, ClassLoader classLoader) {
		AbstractMessageRecognizer recognizer = ClassUtil.newInstance(recognizerConfig.getClassName(),
				AbstractMessageRecognizer.class, classLoader);

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

	private void loadConfig() {
		logger.info("---->>load config：{}", "gateway_" + id + ".xml");
		GatewayConfigParser parser = new GatewayConfigParser();
		config = parser.parse("gateway_" + id + ".xml");
	}

	public static boolean isExceptionMonitorSupport() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isShieldSensitiveFields() {
		// TODO Auto-generated method stub
		return false;
	}

	public static Object getSensitiveReplaceChar() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Object getSensitiveFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isConfigDBSupport() {
		// TODO Auto-generated method stub
		return false;
	}
}
