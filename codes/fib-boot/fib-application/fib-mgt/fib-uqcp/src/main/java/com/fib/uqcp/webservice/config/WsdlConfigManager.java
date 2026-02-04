package com.fib.uqcp.webservice.config;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassLoaderUtil;

public class WsdlConfigManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(WsdlConfigManager.class);

	/** 默认的配置文件存放路径 */
	private static final String DEFAULT_CONFIG_NAME = "wsdl_config";

	/** 默认的配置后缀 */
	private static final String DEFAULT_CONFIG_POSTFIX = ".wsdl";

	/** WSDL文件缓存 */
	private static final Map<String, String> wsdlFileCache = new HashMap<>();

	public static String getWsdlPath(String wsdlFileName) {
		return wsdlFileCache.get(wsdlFileName);
	}

	public static void init() {
		ClassLoader classLoader = ClassLoaderUtil.getClassLoader();
		URL url = classLoader.getResource(DEFAULT_CONFIG_NAME);
		if (null == url) {
			LOGGER.warn("WsdlConfigManager load [{}] is not .", DEFAULT_CONFIG_NAME);
		} else {
			LOGGER.info("load wsdl start ......");
			loadWsdlFile(url);

			Collection<String> filePaths = wsdlFileCache.values();
			if (CollUtil.isNotEmpty(filePaths)) {
				for (String filePath : filePaths) {
					try {
						WebServiceInterfaceManager.loadWsdlByFullPath(filePath);
					} catch (ParseWSDLException e) {
						LOGGER.error("", e);
					}
				}
			}
			LOGGER.info("load wsdl finished ...");
		}
	}

	/**
	 * 加载指定路径下WSDL文件
	 * 
	 * @param url
	 */
	private static void loadWsdlFile(URL url) {
		if (!url.getPath().contains("!")) {
			File dir = new File(url.getPath());
			if (!dir.exists() || dir.isFile()) {
				LOGGER.warn("目录[{}]不存在或是一个文件", dir.getAbsolutePath());
			}
			File[] wsdlDirs = dir.listFiles();
			if (ArrayUtil.isNotEmpty(wsdlDirs)) {
				for (File wsdlDir : wsdlDirs) {
					if (wsdlDir.isDirectory()) {
						File[] wsdlFiles = wsdlDir.listFiles(new SuffixFilter(DEFAULT_CONFIG_POSTFIX));
						if (ArrayUtil.isNotEmpty(wsdlFiles)) {
							for (File wsdlFile : wsdlFiles) {
								String wsdlFilePath = wsdlFile.getAbsolutePath();
								String wsdlFileName = wsdlFile.getName();
								if (!wsdlFileCache.containsKey(wsdlFileName)) {
									wsdlFileCache.put(wsdlFileName, wsdlFilePath);
								} else {
									LOGGER.error("[{}] is repeat.", wsdlFileName);
								}
							}
						}
					}
				}
			}
		}

	}
}
