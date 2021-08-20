package com.fib.commons.detect.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

import com.fib.commons.detect.EncodingDetector;

public class JuniversalchardetEncodingDetector implements EncodingDetector {

	@Override
	public Charset detect(InputStream fis) throws IOException {
		byte[] buf = new byte[4096];
		// (1)
		UniversalDetector detector = new UniversalDetector(null);

		// (2)
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		// (3)
		detector.dataEnd();

		// (4)
		String encoding = detector.getDetectedCharset();
		if (encoding != null) {
			System.out.println("Detected encoding = " + encoding);
		} else {
			System.out.println("No encoding detected.");
		}

		// (5)
		detector.reset();

		return Charset.forName(encoding);
	}

}
