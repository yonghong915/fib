package com.fib.autoconfigure.ruleengine.drools;

import java.io.IOException;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class DroolsRuleEngineConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(DroolsRuleEngineConfig.class);
	private static final String RULES_PATH = "droolRule/";
	private final KieServices kieServices = KieServices.Factory.get();

	@Bean
	KieFileSystem kieFileSystem() throws IOException {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] files = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*.*");
		String path = null;
		for (Resource file : files) {
			path = RULES_PATH + file.getFilename();
			LOGGER.info("path={}", path);
			kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
		}
		return kieFileSystem;
	}

	@Bean
	KieContainer kieContainer() throws IOException {
		KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
		kieBuilder.buildAll();
		return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
	}

	@Bean
	KieBase kieBase() throws IOException {
		return kieContainer().getKieBase();
	}

	@Bean
	KieSession kieSession() throws IOException {
		return kieContainer().newKieSession();
	}

	@Bean
	KModuleBeanFactoryPostProcessor kiePostProcessor() {
		return new KModuleBeanFactoryPostProcessor();
	}
}