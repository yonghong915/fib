package com.fib.order.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.order.annotation.FilterProcess;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 请求过滤器，读取、处理、重写请求体，并传递给后续流程
 */
@Component
@WebFilter(filterName = "requestBodyFilter", urlPatterns = "/*")
@Order(1)
public class RequestBodyFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyFilter.class);
	private final ObjectMapper objectMapper;
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	public RequestBodyFilter(ObjectMapper objectMapper, RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.objectMapper = objectMapper;
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		HandlerExecutionChain handlerChain = null;
		try {
			// 根据请求URI和方法，获取对应的Controller方法链
			handlerChain = requestMappingHandlerMapping.getHandler(httpRequest);
		} catch (Exception e) {
			// 非Controller请求（如静态资源），直接放行
			filterChain.doFilter(request, response);
			return;
		}

		Object handler = handlerChain.getHandler();
		FilterProcess annotation = null;

		if (handler instanceof HandlerMethod handlerMethod) {
			annotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), FilterProcess.class);
			if (annotation == null) {
				annotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), FilterProcess.class);
			}
		}

		if (annotation == null) {
			filterChain.doFilter(request, response);
			return;
		}

		ServletRequest wrappedRequest = null;
		if (request instanceof HttpServletRequest) {
			wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
		}
		if (wrappedRequest == null) {
			filterChain.doFilter(request, response);
		} else {
			process((ContentCachingRequestWrapper) wrappedRequest);
			filterChain.doFilter(wrappedRequest, response);
		}
	}

	private void process(ContentCachingRequestWrapper wrappedRequest) throws IOException {
		wrappedRequest.getReader().readLine();
		String originalBody = new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
		LOGGER.info("Orig Request:[{}]", originalBody);

		/* 处理请求体 */
		String modifiedBody = processRequestBody(originalBody);
		LOGGER.info("Modified Request:[{}]", modifiedBody);

		byte[] modifiedBodyBytes = modifiedBody.getBytes(StandardCharsets.UTF_8);
		wrappedRequest.getInputStream().close(); // 关闭原流
		// 替换为新的输入流（包含修改后的内容）
		ServletInputStream newInputStream = new DelegatingServletInputStream(new ByteArrayInputStream(modifiedBodyBytes));

		try {
			Field inputStreamField = ContentCachingRequestWrapper.class.getDeclaredField("inputStream");
			inputStreamField.setAccessible(true);
			inputStreamField.set(wrappedRequest, newInputStream);
			// 同时更新缓存的内容（确保 getContentAsByteArray() 也能获取到修改后的数据）
			Field cachedContentField = ContentCachingRequestWrapper.class.getDeclaredField("cachedContent");
			cachedContentField.setAccessible(true);

			FastByteArrayOutputStream aa = new FastByteArrayOutputStream(modifiedBodyBytes.length);
			aa.write(modifiedBodyBytes);
			cachedContentField.set(wrappedRequest, aa);
		} catch (Exception e) {
			LOGGER.error("Failed to rewrite reqeust body", e);
			throw new RuntimeException("Failed to rewrite reqeust body", e);
		}
	}

	private String processRequestBody(String originalBody) {
		try {
			Map<String, Object> userMap = objectMapper.readValue(originalBody, new TypeReference<Map<String, Object>>() {
			});
			Object obj = userMap.get("data");
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error("Failed to parse json", e);
		}
		return originalBody;
	}

	private static class DelegatingServletInputStream extends ServletInputStream {
		private final ByteArrayInputStream inputStream;

		public DelegatingServletInputStream(ByteArrayInputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public boolean isFinished() {
			return inputStream.available() == 0;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			// 无需实现，非异步场景
		}
	}
}