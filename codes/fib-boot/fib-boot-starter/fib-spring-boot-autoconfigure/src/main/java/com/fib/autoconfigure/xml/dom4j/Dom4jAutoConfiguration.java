package com.fib.autoconfigure.xml.dom4j;

import org.dom4j.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.xml.IXmlService;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Document.class)
public class Dom4jAutoConfiguration {

	@Bean("dom4jService")
	@ConditionalOnMissingBean(IXmlService.class)
	IXmlService dom4jParseService() {
		return new Dom4jService();
	}
}