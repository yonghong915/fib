package com.fib.commons.detect.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.any23.encoding.TikaEncodingDetector;

import com.fib.commons.detect.EncodingDetector;

public class Any23EncodingDetector implements EncodingDetector {

	@Override
	public Charset detect(InputStream is) throws IOException {
		return Charset.forName(new TikaEncodingDetector().guessEncoding(is));
	}
}
