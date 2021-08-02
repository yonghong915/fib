package com.fib.upp.service.gateway;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fib.commons.exception.CommonException;
import com.fib.upp.service.gateway.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.upp.service.gateway.message.metadata.MessageMetaData;
import com.fib.upp.service.gateway.message.metadata.MessageMetadataManager;
import com.fib.upp.service.gateway.module.ModuleEntity;
import com.fib.upp.xml.config.ChannelConfig;
import com.fib.upp.xml.config.ChannelMainConfig;
import com.fib.upp.xml.config.GatewayConfig;
import com.fib.upp.xml.config.ModuleConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;

@Service
public class CommGateway {
	private String id;
	private GatewayConfig config = new GatewayConfig();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
	@Autowired
	IConfig gatewayConfigService;

	@Autowired
	IConfig messageBeanConfigService;
	/**
	 * 通道部署根路径
	 */
	private String deployPath;

	public void setId(String id) {
		this.id = id;
	}

	public void start() {
		loadConfig();
		String variableFileName = config.getVariableFileName();

		// 加载模块
		loadModules();

		// 加载通道
		loadChannels();

		// 启动事件处理器

		// 启动通道
		startChannels();

		// 启动监控
		startMonitor();
	}

	private void startMonitor() {
		logger.info("startMonitor...");
	}

	private void startChannels() {
		logger.info("startChannels...");

	}

	private void loadChannels() {
		logger.info("loadChannels...");
		ChannelMainConfig channelMainConfig = null;
		Iterator<ChannelMainConfig> it = config.getChannels().values().iterator();
		while (it.hasNext()) {
			channelMainConfig = (ChannelMainConfig) it.next();
			if (channelMainConfig.isStartup()) {
				loadChannel(channelMainConfig);
			}
		}
	}

	private void loadChannel(ChannelMainConfig channelMainConfig) {
		logger.info("loadChannel....channelId=[{}]", channelMainConfig.getId());

		String channelDeployPath = deployPath + channelMainConfig.getDeploy();
		if (!channelDeployPath.endsWith(FileUtil.FILE_SEPARATOR)) {
			channelDeployPath += FileUtil.FILE_SEPARATOR;
		}

		File channelDeployDir = new File(channelDeployPath);
		if (!FileUtil.isDirectory(channelDeployDir)) {
			throw new RuntimeException(channelDeployDir + " is not directory.");
		}

		ChannelConfig channelConfig = null;
		channelConfig = new ChannelConfig();
		channelConfig.setMainConfig(channelMainConfig);
		List<String> modifiedFiles = generateSourceFile(channelDeployPath, channelConfig);

		if (CollUtil.isNotEmpty(modifiedFiles)) {
			// ClasspathUtil.compileFiles(modifiedFiles, srcRootPath, classRootPath,
			// "UTF-8");
			// ExtensionLoader.getExtensionLoader();
			
		}
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

		Map<String, MessageMetaData> allMsgInGroup = MessageMetadataManager.loadMetadataGroup(channelId,
				messageBeanDir);

		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);
		Iterator<MessageMetaData> iter = allMsgInGroup.values().iterator();
		MessageMetaData msg = null;
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

	private void loadModules() {
		logger.info("loadModules...");
		if (null == config.getModules()) {
			return;
		}

		Iterator<ModuleConfig> it = config.getModules().iterator();
		ModuleConfig moduleConfig = null;
		ModuleEntity module = null;
		while (it.hasNext()) {
			moduleConfig = it.next();
			// logger.error("---->>>> load module:" +
			// moduleConfig.getClassName());
			module = ReflectUtil.newInstance(moduleConfig.getClassName());
			module.setParameters(moduleConfig.getParameters());
			module.initialize();
		}
	}

	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
		if (!this.deployPath.endsWith("/")) {
			this.deployPath += "/";
		}
	}

	private void loadConfig() {
		// config\\gateway_cnaps2.xml
		String filePath = "config\\gateway_" + id + ".xml";
		config = (GatewayConfig) gatewayConfigService.load(filePath);
	}
}
