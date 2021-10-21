package com.fib.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.detect.EncodingDetector;

public class EncodingDetectorTest {
	private static final Logger logger = LoggerFactory.getLogger(EncodingDetectorTest.class);

	@Test
	public void testProtoStuff() {
		String filePath = "F:\\logs\\test.txt";
		String content = "用于Unicode家族编码的测定个a ge ege e UnicodeDetector用于Unicode家族编码的测定";
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("GBK")));) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		EncodingDetector encodingDetector = ScopeModelUtil.getExtensionLoader(EncodingDetector.class, null)
				.getDefaultExtension();

		Charset charset = null;
		try (InputStream is = new File(filePath).toURI().toURL().openStream();) {
			charset = encodingDetector.detect(is);
			logger.info("charset={}", charset.name());

			encodingDetector = ScopeModelUtil.getExtensionLoader(EncodingDetector.class, null).getExtension("any23");
			charset = encodingDetector.detect(is);
			logger.info("charset={}", charset.name());

			encodingDetector = ScopeModelUtil.getExtensionLoader(EncodingDetector.class, null)
					.getExtension("juniversalchardet");
			charset = encodingDetector.detect(is);
			logger.info("charset={}", charset.name());
		} catch (IOException e) {

		}
	}
}
