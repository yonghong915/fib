package com.fib.commons.detect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.dubbo.common.extension.SPI;

/**
 * 编码探测器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-20
 */
@SPI("cpdetector")
public interface EncodingDetector {
	static final String DEFAULT_CHARSET = "GBK";

	Charset detect(InputStream input) throws IOException;

//	private EncodingDetector() {
//		// noting to do
//	}
//
//	private static class SingletonHolder {
//		private static final EncodingDetector instance =  new EncodingDetector();
//	}
//
//	public static EncodingDetector getInstance() {
//		return SingletonHolder.instance;
//	}

}