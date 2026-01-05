package com.fib.midbiz.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.fib.commons.xml.dom4j.XmlUtils;

public class TestDemo {
	public static void main(String[] args) {
		String aa = Base64.getEncoder().encodeToString("天a1234456天".getBytes());
		System.out.println(aa);

		byte[] desByte = Base64.getDecoder().decode(aa.getBytes());
		System.out.println(new String(desByte));

		Company company = new Company();
		String xml;
		try {
			xml = XmlUtils.toXml(company);
			System.out.println(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
//		ServletContainerInitializer
//		AbstractAnnotationConfigDispatcherServletInitializer
//		DispatcherServletInitializer 
//		SpringServletContainerInitializer
//		WebMvcAutoConfiguration
		TestRecored a = new TestRecored("aa",1, 6);
		System.out.println(a.age());
	}

	public static String asXML(OutputFormat format, Document doc) {
		format.setEncoding("UTF-8");
		try {
			StringWriter out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(doc);
			writer.flush();
			return out.toString();
		} catch (IOException e) {
			throw new RuntimeException("IOException while generating textual " + "representation: " + e.getMessage());
		}
	}
}
