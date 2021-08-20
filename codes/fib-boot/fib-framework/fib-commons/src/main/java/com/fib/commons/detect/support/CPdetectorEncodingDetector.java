package com.fib.commons.detect.support;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import com.fib.commons.detect.EncodingDetector;
import com.fib.commons.util.constant.EnumConstants;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class CPdetectorEncodingDetector implements EncodingDetector {

	/**
	 * 利用第三方开源包cpdetector获取文件编码格式
	 * 
	 * @param path 要判断文件编码格式的源文件的路径
	 * @author
	 */
	public String getFileEncode(String path) {
		File file = new File(path);
		try {
			return detect(file.toURI().toURL());
		} catch (IOException e) {

		}
		return null;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public String getTextEncode(String text) {
		InputStream in = new ByteArrayInputStream(text.getBytes());
		try {
			return detect(in, text.length());
		} catch (IOException e) {

		}
		return null;
	}

	public String detect(URL url) throws IOException {
		CodepageDetectorProxy detector = buildDetector();

		Charset charset = detector.detectCodepage(url);
		return (null == charset) ? DEFAULT_CHARSET : charset.name();
	}

	public String detect(InputStream in, int len) throws IOException {
		CodepageDetectorProxy detector = buildDetector();
		Charset charset = detector.detectCodepage(in, len);
		return (null == charset) ? DEFAULT_CHARSET : charset.name();
	}

	/**
	 * 构造探测器
	 * 
	 * @return
	 */
	public CodepageDetectorProxy buildDetector() {
		/*
		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
		 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
		 * JChardetFacade、ASCIIDetector、UnicodeDetector。
		 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
		 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
		 * cpDetector是基于统计学原理的，不保证完全正确。
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/*
		 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 指示是否显示探测过程的详细信息，为false不显示。
		 */
		detector.add(new ParsingDetector(EnumConstants.LogicType.FALSE.isLogicFlag()));
		/*
		 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
		 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
		 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
		 */
		detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
		// ASCIIDetector用于ASCII编码测定
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector用于Unicode家族编码的测定
		detector.add(UnicodeDetector.getInstance());
		return detector;
	}

	@Override
	public Charset detect(InputStream is) throws IOException {
		CodepageDetectorProxy detector = buildDetector();
		return detector.detectCodepage(is, is.available());
	}
}
