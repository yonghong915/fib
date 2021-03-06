package com.fib.gateway.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.gateway.netty.common.Constants;

public class StringUtils {
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
	private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); // key value

	/**
	 * parse query string to Parameters.
	 *
	 * @param qs query string.
	 * @return Parameters instance.
	 */
	public static Map<String, String> parseQueryString(String qs) {
		if (qs == null || qs.length() == 0)
			return new HashMap<String, String>();
		return parseKeyValuePair(qs, "\\&");
	}

	private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
		String[] tmp = str.split(itemSeparator);
		Map<String, String> map = new HashMap<String, String>(tmp.length);
		for (int i = 0; i < tmp.length; i++) {
			Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
			if (matcher.matches() == false)
				continue;
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	} // pair
}
