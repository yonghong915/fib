package com.fib.order.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * 接口安全过滤器：统一处理加解密、签名验签、防重放
 */
@Component
@WebFilter(urlPatterns = "/*") // 仅拦截/api开头的接口
@Order(0)
public class ApiSecurityFilter implements Filter {

	@Autowired
	private SecurityUtils securityUtils;
	@Autowired
	private ObjectMapper objectMapper;
//	@Autowired
//	private StringRedisTemplate redisTemplate;

	// 配置参数
	// @Value("${api.security.timeout}")
	private long timeout = 30000;
	// @Value("#{${api.security.client}}")
	private Map<String, Map<String, String>> clientConfig;
	// @Value("${api.security.nonce.cache.expire}")
	private long nonceExpire = 30000;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");

		// ========== 步骤1：包装请求，读取加密请求体 ==========
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
		wrappedRequest.getReader().readLine();
		String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
		EncryptRequest encryptRequest;
		try {
			encryptRequest = objectMapper.readValue(requestBody, EncryptRequest.class);
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "400", "请求格式错误：" + e.getMessage());
			return;
		}

		// ========== 步骤2：基础参数校验 ==========
		if (encryptRequest.getAppId() == null || encryptRequest.getTimestamp() == null || encryptRequest.getNonce() == null
				|| encryptRequest.getSign() == null || encryptRequest.getCipherText() == null) {
			writeErrorResponse(httpResponse, "400", "缺少必要的安全参数");
			return;
		}

		// ========== 步骤3：校验AppId和密钥 ==========
//		Map<String, String> clientKeys = clientConfig.get(encryptRequest.getAppId());
//		if (clientKeys == null) {
//			writeErrorResponse(httpResponse, "401", "无效的AppId");
//			return;
//		}
//		String aesKey = clientKeys.get("aes-key");
//		String signKey = clientKeys.get("sign-key");
		String aesKey = "1234567890123456";
		String signKey = "signKey1234567890";
		// ========== 步骤4：防重放校验（时间戳 + nonce） ==========
		long requestTime = encryptRequest.getTimestamp();
		long currentTime = System.currentTimeMillis();
		// 校验时间戳是否超时
		if (Math.abs(currentTime - requestTime) > timeout) {
			writeErrorResponse(httpResponse, "403", "请求已超时（超时时间5分钟）");
			return;
		}
		// 校验nonce是否已使用（防重放）
		String nonceKey = "api:nonce:" + encryptRequest.getNonce() + ":timestamp:" + requestTime;
//		if (redisTemplate.hasKey(nonceKey)) {
//			writeErrorResponse(httpResponse, "403", "重复请求（nonce已使用）");
//			return;
//		}
		// 缓存nonce
		// redisTemplate.opsForValue().set(nonceKey, "1", nonceExpire,
		// TimeUnit.SECONDS);

		// ========== 步骤5：验签 ==========
		String signContent = encryptRequest.getAppId() + encryptRequest.getTimestamp() + encryptRequest.getNonce()
				+ encryptRequest.getCipherText();
		boolean signValid;
		try {
			signValid = securityUtils.verifySign(signContent, signKey, encryptRequest.getSign());
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "500", "签名验证失败：" + e.getMessage());
			return;
		}
		if (!signValid) {
			writeErrorResponse(httpResponse, "403", "签名无效");
			return;
		}

		// ========== 步骤6：解密业务报文 ==========
		String businessContent;
		try {
			businessContent = securityUtils.aesDecrypt(encryptRequest.getCipherText(), aesKey);
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "500", "报文解密失败：" + e.getMessage());
			return;
		}

		// ========== 步骤7：重置请求体为解密后的业务报文，传递给Controller ==========
		byte[] businessBytes = businessContent.getBytes(StandardCharsets.UTF_8);
		// 自定义包装类，重置请求体
		CustomContentCachingRequestWrapper customRequest = new CustomContentCachingRequestWrapper(httpRequest);
		customRequest.setCachedContent(businessBytes);

		// ========== 步骤8：自定义响应包装，捕获Controller响应并加密 ==========
		CustomResponseWrapper customResponse = new CustomResponseWrapper(httpResponse);
		chain.doFilter(customRequest, customResponse);

		// ========== 步骤9：加密响应报文并返回 ==========
		try {
			// 读取Controller的原始响应
			String originalResponse = new String(customResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
			BusinessResponse<?> businessResponse = objectMapper.readValue(originalResponse, BusinessResponse.class);

			// 加密业务响应
			String encryptedResponse = securityUtils.aesEncrypt(originalResponse, aesKey);
			// 生成响应签名
			String responseSign = securityUtils.hmacSign(encryptedResponse + encryptRequest.getNonce(), signKey);

			// 封装加密响应
			/*
			 * EncryptResponse encryptResponse = new EncryptResponse();
			 * encryptResponse.setCode(businessResponse.getCode());
			 * encryptResponse.setMsg(businessResponse.getMsg());
			 * encryptResponse.setCipherText(encryptedResponse);
			 * encryptResponse.setSign(responseSign);
			 * 
			 * // 写入响应 httpResponse.getWriter().write(objectMapper.writeValueAsString(
			 * encryptResponse));
			 */
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "500", "响应加密失败：" + e.getMessage());
		}
	}

	/**
	 * 写入错误响应
	 */
	private void writeErrorResponse(HttpServletResponse response, String code, String msg) throws IOException {
		/*
		 * EncryptResponse errorResponse = new EncryptResponse();
		 * errorResponse.setCode(code); errorResponse.setMsg(msg);
		 * response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		 */
	}

	/**
	 * 自定义请求包装类：支持重置缓存内容
	 */
	private static class CustomContentCachingRequestWrapper extends ContentCachingRequestWrapper {
		public CustomContentCachingRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		public void setCachedContent(byte[] content) {
			// cachedContent = content;
			// this.contentLength = content.length;
		}
	}

	/**
	 * 自定义响应包装类：捕获响应内容
	 */
	private static class CustomResponseWrapper extends HttpServletResponseWrapper {
		private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		private final PrintWriter writer = new PrintWriter(outputStream);

		public CustomResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return new ServletOutputStream() {
				@Override
				public void write(int b) throws IOException {
					outputStream.write(b);
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setWriteListener(WriteListener writeListener) {
					// TODO Auto-generated method stub

				}
			};
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return writer;
		}

		/**
		 * 获取响应内容字节数组
		 */
		public byte[] getContentAsByteArray() {
			writer.flush();
			return outputStream.toByteArray();
		}
	}
}
