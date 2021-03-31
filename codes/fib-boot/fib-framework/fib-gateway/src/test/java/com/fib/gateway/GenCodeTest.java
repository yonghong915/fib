package com.fib.gateway;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.ClasspathUtil;
import com.fib.gateway.channel.config.ChannelConfig;
import com.fib.gateway.config.ChannelMainConfig;

import cn.hutool.core.collection.CollUtil;

public class GenCodeTest {
	public static void main(String[] args) {
		String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\npc_cnaps2_recv\\";
		CommGateway cg = new CommGateway();
		ChannelConfig channelConfig = new ChannelConfig();
		ChannelMainConfig mainConfig = new ChannelMainConfig();
		mainConfig.setId("BEPS_121_bean");
		channelConfig.setMainConfig(mainConfig);
		List<String> modifiedFiles = cg.generateSourceFile(channelDeployPath, channelConfig);
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
		
		if (CollUtil.isNotEmpty(modifiedFiles)) {
			ClasspathUtil.compileFiles(modifiedFiles, srcRootPath, classRootPath, "UTF-8");
		}
		// assertEquals("D:\\upp\\upp_recv\\", channelDeployPath);
		// ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension().compile(code,
		// classLoader);
	}
}
