package com.fib.uqcp.message;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.dubbo.common.compiler.Compiler;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.fib.commons.xml.dom4j.Dom4jParser;

import cn.hutool.core.io.FileUtil;

public class MessagePacker {

	public static void main(String[] args) {
		MessagePacker packer = new MessagePacker();
		String file = "./deploy\\message-bean\\MsgHeader.xml";
		packer.parse(file);
	}

	public void parse(String file) {
		Document document = Dom4jParser.newBuilder().build().getDocument(FileUtil.getInputStream(file));
		Element rootEle = document.getRootElement();
		String id = rootEle.attributeValue("id");
		String eleClass = rootEle.attributeValue("class");
		List<Node> nodeLst = document.selectNodes("/message-bean/field");
		for (Node node : nodeLst) {
			if (node instanceof Element ele) {
				String attrVal = ele.attributeValue("name");
				System.out.println(attrVal);
			}
		}
		System.out.println("aaa=" + eleClass);
		
		
		
		
//		Class<?> clazz = ClassLoaderUtil.loadClass(eleClass);
//		System.out.println(clazz);
		//Compiler compiler = ApplicationModel.defaultModel().getDefaultModule().getExtensionLoader(Compiler.class).getDefaultExtension();

		Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");
		String code = "class MsgHeader{}";
		// Class<?> clazz = compiler.compile(null, code,
		// ClassLoaderUtil.getClassLoader());
		Matcher matcher = CLASS_PATTERN.matcher(code);
		System.out.println(matcher.find());
	}
}
