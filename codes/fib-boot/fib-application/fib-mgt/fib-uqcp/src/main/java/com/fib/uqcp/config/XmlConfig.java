package com.fib.uqcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 读取xml配置类
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-05 15:09:36
 */
@Configuration
@ImportResource("classpath:meta-config.xml")
public class XmlConfig {

}
