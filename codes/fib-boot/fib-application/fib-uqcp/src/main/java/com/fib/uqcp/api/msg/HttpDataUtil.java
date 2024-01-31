package com.fib.uqcp.api.msg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;

public class HttpDataUtil {
	/**
	 * post请求处理：获取 Body 参数，转换为SortedMap 堆代码 duidaima.com
	 * 
	 * @param request
	 */
	public SortedMap<String, String> getBodyParams(final HttpServletRequest request) throws IOException {
		byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
		String body = new String(requestBody);
		return JSONUtil.toBean(body, SortedMap.class);
	}

	/**
	 * get请求处理：将URL请求参数转换成SortedMap
	 */
	public static SortedMap<String, String> getUrlParams(HttpServletRequest request) {
		String param = "";
		SortedMap<String, String> result = new TreeMap<>();
		if (StringUtils.isEmpty(request.getQueryString())) {
			return result;
		}
		try {
			param = URLDecoder.decode(request.getQueryString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String[] params = param.split("&");
		for (String s : params) {
			String[] array = s.split("=");
			result.put(array[0], array[1]);
		}
		return result;
	}
}
