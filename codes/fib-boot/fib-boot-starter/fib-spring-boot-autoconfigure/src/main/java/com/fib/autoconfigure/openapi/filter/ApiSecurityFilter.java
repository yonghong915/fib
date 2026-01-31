package com.fib.autoconfigure.openapi.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.openapi.JsonRequestWrapper;
import com.fib.autoconfigure.openapi.ResponseBuilder;
import com.fib.autoconfigure.openapi.config.NacosConfig;
import com.fib.autoconfigure.openapi.message.ApiRequest;
import com.fib.autoconfigure.openapi.message.ApiResponse;
import com.fib.autoconfigure.openapi.message.RequestHeader;
import com.fib.autoconfigure.openapi.util.EncryptUtils;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiSecurityFilter.class);

	@Autowired
	private NacosConfig nacosConfig;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	ResponseBuilder responseBuilder;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");

		// ========== 步骤1：包装请求，读取加密请求体 ==========
		JsonRequestWrapper wrappedRequest = new JsonRequestWrapper(httpRequest);
		// wrappedRequest.getReader().readLine();
		String requestBody = wrappedRequest.getRequestBodyJson();// String(wrappedRequest.getContentAsByteArray(),
																	// StandardCharsets.UTF_8);
		if ("{}".equals(requestBody)) {
			chain.doFilter(request, response);
			return;
		}
		ApiRequest<?> apiRequest;
		try {
			apiRequest = SingletonObjectMapper.fromJson(requestBody, ApiRequest.class);
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "400", "请求格式错误：" + e.getMessage());
			return;
		}

		RequestHeader requestHeader = apiRequest.getRequestHeader();
		// ========== 步骤2：基础参数校验 ==========
		if (requestHeader.getAppId() == null || requestHeader.getTimestamp() == null || requestHeader.getNonce() == null
				|| requestHeader.getSign() == null) {
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
//		String aesKey = "1234567890123456";
//		String signKey = "signKey1234567890";
		// ========== 步骤4：防重放校验（时间戳 + nonce） ==========
		long requestTime = requestHeader.getTimestamp();
		long currentTime = System.currentTimeMillis();
		// 校验时间戳是否超时
		if (Math.abs(currentTime - requestTime) > nacosConfig.getTimeout()) {
			writeErrorResponse(httpResponse, "403", "请求已超时（超时时间5分钟）");
			return;
		}

		/* 步骤6：解密业务报文 */
		String encryptBizData = apiRequest.getRequestBody().getEncryptedBody();
		LOGGER.info("encryptBizData=[{}]", encryptBizData);

		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJp4BnASzcnfBN58NFSjH61jKv+Np5aiXHmuY/LCTOyY0UjZBzcdBvgDefCpvAMTv7QsqCtX4B1V5HfHdW5z5/imJl6W6mwBtixcwfhqO+Fn6/wmG2AugmQBh6T0Se7peG6KJUTu1mEOExR7lGvY8hBNRngwdcJYSLx7bgcIfqu9AgMBAAECgYACITVY2RAwdZb2rf3htzBiCVvn00SULC+8DMEQ291y0KY9YSKl6kxKN0VjNvqM4fo9ah/fyHHNAxNn6bPZ3pa8PnyMknu/B8zU22/7DI9Kshm/6xxLsYs2TYdJfekcbPdnozWjtwoedCwASKwVz0ExHYzJ2ZfpST3zc2xVHUZ1AQJBANYwp72KTWcnYO6+HY07K/Dxv1VLe9VtiuanD/Mq14twRXICNcOXPth+ciVQUSR60vbVN6nTdlCjS1gZfo75NL0CQQC4nwbCPf/l42QPpVNVCEk4nbq39hPBJ/fY9QW1VCpBuYLBUQBiwLqrT7TSTSDdf8CWGQb/uZYxFb7xq2f7GEMBAkBRcb7Wu7gi+T5KidAC2/UhcUsny8QSq8ydV/kgpbHAO7isWVrIPMKQ38PXnGq+TFXbtcess9PRZcZIgak2BFyhAkBqIHg5Jny4gKtfVxD9G2ND2V+hKiKW8UvG+qqKXtRfra0dRVvsaI+ltI7kKRQQX8SsQ7zDOcK9epulvntqWrsBAkA5Nq7Ag4Yc7kF/+m3RRloaJ+AZXhHkEoLuLSi+Vu0I/IaeZwcocLWlGUUoTTynzC6BU8imcj8n+scH4RFnxm3w";
		String keyvStr = requestHeader.getEncryptedAesKey();
		String bizData;
		try {
			String[] ivStr = EncryptUtils.rsaDecryptAesKey(EncryptUtils.RSA_ALGORITHM, keyvStr, privateKey);
			bizData = EncryptUtils.aesDecrypt(encryptBizData, ivStr[0], ivStr[1]);
			LOGGER.info("bizData=[{}]", bizData);
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "500", "报文解密失败：" + e.getMessage());
			return;
		}

		// 校验nonce是否已使用（防重放）
		String nonceKey = "api:nonce:" + requestHeader.getNonce() + ":timestamp:" + requestTime;
		if (redisTemplate.hasKey(nonceKey)) {
			writeErrorResponse(httpResponse, "403", "重复请求（nonce已使用）");
			return;
		}

		// 缓存nonce
		redisTemplate.opsForValue().set(nonceKey, "1", nacosConfig.getNonceExpire(), TimeUnit.MILLISECONDS);

		/* 步骤5：验签 */
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaeAZwEs3J3wTefDRUox+tYyr/jaeWolx5rmPywkzsmNFI2Qc3HQb4A3nwqbwDE7+0LKgrV+AdVeR3x3Vuc+f4piZelupsAbYsXMH4ajvhZ+v8JhtgLoJkAYek9Enu6XhuiiVE7tZhDhMUe5Rr2PIQTUZ4MHXCWEi8e24HCH6rvQIDAQAB";
		String signContent = requestHeader.getAppId() + requestHeader.getTimestamp() + requestHeader.getNonce()
				+ bizData;
		String signSourceHash = DigestUtil.sha256Hex(signContent);
		boolean signValid = false;
		try {
			signValid = EncryptUtils.verifySign(SignAlgorithm.SHA256withRSA.getValue(), signSourceHash,
					requestHeader.getSign(), publicKey);
		} catch (Exception e) {
			writeErrorResponse(httpResponse, "500", "签名验证失败：" + e.getMessage());
			return;
		}
		LOGGER.info("message request signValid=[{}]", signValid);
		if (!signValid) {
			writeErrorResponse(httpResponse, "403", "签名无效");
			return;
		}

		// ========== 步骤7：重置请求体为解密后的业务报文，传递给Controller ==========
		wrappedRequest.getInputStream().close(); // 关闭原流
		wrappedRequest.resetJsonRequestBody(bizData);

		// ========== 步骤8：自定义响应包装，捕获Controller响应并加密 ==========
		CustomResponseWrapper customResponse = new CustomResponseWrapper(httpResponse);
		chain.doFilter(wrappedRequest, customResponse);

		// ========== 步骤9：加密响应报文并返回 ==========
		try {
			// 读取Controller的原始响应
			String originalResponse = new String(customResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
			Object respBizObject = originalResponse;
			if (JSONUtil.isTypeJSON(originalResponse)) {
				respBizObject = SingletonObjectMapper.fromJson(originalResponse, Object.class);
			}
			ApiResponse<Object> apiResponse = responseBuilder.buildMessageResponse(respBizObject);

			// BusinessResponse<?> businessResponse =
			// objectMapper.readValue(originalResponse, BusinessResponse.class);

			// 加密业务响应
			// String encryptedResponse = securityUtils.aesEncrypt(originalResponse,
			// aesKey);
			// 生成响应签名
			// String responseSign = securityUtils.hmacSign(encryptedResponse +
			// encryptRequest.getNonce(), signKey);

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
			apiResponse.getResponseHeader().setRequestId(requestHeader.getRequestId());
			httpResponse.getWriter().write(SingletonObjectMapper.toSortedJson(apiResponse));
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
					//
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
