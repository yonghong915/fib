package com.fib.commons.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dom4j.Document;

import com.fib.upp.xml.config.GatewayConfigParser;



public class TestI {

	public static void main(String[] args) {
		Document document;
		String filePath = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config\\gateway_cnaps2.xml";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<?> gatewayConfigParserClazz = null;

		try {
			gatewayConfigParserClazz = classLoader.loadClass("com.fib.xml.config.GatewayConfigParser");
			Object obj = gatewayConfigParserClazz.getDeclaredConstructor().newInstance();
			Method method = gatewayConfigParserClazz.getMethod("parse", String.class);
			Object retObj = method.invoke(obj, filePath);
			System.out.println("retObj=" + retObj);

			Object retObj1 = method.invoke(obj, filePath);
			System.out.println("retObj1=" + retObj1);

			Object retObj2 = new GatewayConfigParser().parse(filePath);
			System.out.println("retObj2=" + retObj2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
