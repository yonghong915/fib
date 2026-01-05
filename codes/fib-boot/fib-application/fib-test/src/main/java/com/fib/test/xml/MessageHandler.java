//package com.fib.test.xml;
//
//import java.io.File;
//
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.ElementHandler;
//import org.dom4j.ElementPath;
//import org.dom4j.io.SAXReader;
//
//import cn.hutool.core.io.FileUtil;
//
//public class MessageHandler implements ElementHandler {
//
//	@Override
//	public void onStart(ElementPath elementPath) {
//		Element elt = elementPath.getCurrent();
//		String attrVal = elt.attributeValue("id");
//		System.out.println("attrVal=" + attrVal);
//
//		attrVal = elt.attributeValue("type");
//		System.out.println("attrVal=" + attrVal);
//	}
//
//	@Override
//	public void onEnd(ElementPath elementPath) {
//		Element elt = elementPath.getCurrent();
//		System.out.println(elt.getName() + " : " + elt.getText());
//		String elePath = elementPath.getPath();
//		System.out.println("elePath=" + elePath);
//	}
//
//	public static void main(String[] args) {
//		SAXReader saxReader = new SAXReader();
//		ElementHandler handler = new MessageHandler();
//		saxReader.addHandler("/message-bean", handler);
//		saxReader.addHandler("/message-bean/field", handler);
//		saxReader.addHandler("/message-bean/template", handler);
//		try {
//			saxReader.read(FileUtil.file("config/xml/deploy/CCMS_990_Out.xml"));
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//	}
//}
