package com.fib.msgconverter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.dubbo.common.compiler.Compiler;
import org.apache.dubbo.common.extension.ExtensionLoader;

import com.fib.msgconverter.message.bean.MessageBean;

import cn.hutool.core.io.FileUtil;

public class CompilerTest {

	public static void main(String[] args) {
		ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
		Compiler compiler = loader.getExtension("jdk");
	
		String filePath = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-msg-converter\\outpro\\src\\com\\fib\\msgconverter\\message\\bean\\HelloBean.java";
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String code = FileUtil.readUtf8String(filePath);
		Class<?> clazz = compiler.compile(code, classloader);

		try {
			Object obj = clazz.getDeclaredConstructor().newInstance();
			System.out.println(clazz.getName());
			System.out.println(clazz.getSimpleName());
			MessageBean mb = (MessageBean) obj;
			mb.validate();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
		}

		String str1 = new String("1") + new String("2");
		str1.intern();
		System.out.println(str1 == "12");

		String str2 = "3" + new String("4");
		System.out.println(str2 == "34");

		String str3 = new String("56");
		System.out.println(str3 == "56");

		String str4 = new String("78");
		System.out.println(str4.intern() == "78");
		
	}
}
