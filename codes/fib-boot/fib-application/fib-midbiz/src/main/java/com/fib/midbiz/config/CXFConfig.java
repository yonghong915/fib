package com.fib.midbiz.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fib.midbiz.cxf.service.UserService;

import jakarta.xml.ws.Endpoint;

@Configuration
public class CXFConfig {
	@Autowired
	private Bus bus;

	@Autowired
	private UserService userService;

	/**
	 * 此方法被注释后:wsdl访问地址为http://127.0.0.1:8080/services/user?wsdl
	 * 去掉注释后：wsdl访问地址为：http://127.0.0.1:8080/soap/user?wsdl
	 */
	@Bean
	ServletRegistrationBean<CXFServlet> createServletRegistrationBean() {
		return new ServletRegistrationBean<CXFServlet>(new CXFServlet(), "/soap/*");
	}

	/**
	 * 发布服务 指定访问url
	 * 
	 * @return
	 */
	@Bean
	Endpoint userEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, userService);
		endpoint.publish("/user");
//		endpoint.getInInterceptors().add(new II());
//		endpoint.getOutInterceptors().add(new ());
		return endpoint;
	}
}
