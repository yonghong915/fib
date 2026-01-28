//package com.fib.mall.message;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.lang.Assert;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.RSA;
//
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
///**
// * 签名验签工具类
// */
//public class SignUtils {
//
//	// 私钥（实际项目中应从配置中心/密钥管理系统获取）
//	private static final String PRIVATE_KEY = "你的RSA私钥";
//	// 公钥（实际项目中应按appId维护公钥列表）
//	private static final String PUBLIC_KEY = "你的RSA公钥";
//	// 签名过期时间（5分钟）
//	private static final long SIGN_EXPIRE_TIME = 5 * 60 * 1000L;
//
//	/**
//	 * 生成签名
//	 * 
//	 * @param obj        待签名对象
//	 * @param privateKey 私钥
//	 * @return 签名值
//	 */
////	public static String generateSign(Object obj, String privateKey) {
////		// 1. 对象转JSON并按key排序
////		String sortedJson = getSortedJsonString(obj);SecureUtil
////
////		// 2. SHA256摘要 + RSA签名
////		RSA rsa = new RSA(privateKey, null);
////		byte[] signBytes = rsa.sign(sortedJson.getBytes(StandardCharsets.UTF_8), KeyType.PrivateKey);
////		SM2
////		// 3. Base64编码
////		return Base64.encode(signBytes);
////	}
//
//	/**
//	 * 验证签名
//	 * 
//	 * @param obj       待验签对象
//	 * @param sign      签名值
//	 * @param publicKey 公钥
//	 * @return 是否验签通过
//	 */
////	public static boolean verifySign(Object obj, String sign, String publicKey) {
////		if (StrUtil.isBlank(sign)) {
////			return false;
////		}
////
////		// 1. 对象转JSON并按key排序
////		String sortedJson = getSortedJsonString(obj);
////
////		// 2. Base64解码签名
////		byte[] signBytes = Base64.decode(sign);
////
////		// 3. RSA验签
////		RSA rsa = new RSA(null, publicKey);
////		return rsa.verify(sortedJson.getBytes(StandardCharsets.UTF_8), signBytes, KeyType.PublicKey);
////	}
//
//	/**
//	 * 验证请求签名（包含时间戳校验）
//	 * 
//	 * @param request   请求对象
//	 * @param publicKey 公钥
//	 * @return 是否验签通过
//	 */
////	public static boolean verifyRequestSign(ApiRequest<?> request, String publicKey) {
////		Assert.notNull(request, "请求对象不能为空");
////		Assert.notNull(request.getRequestHeader(), "请求头不能为空");
////
////		RequestHeader header = request.getRequestHeader();
////
////		// 1. 校验时间戳（防止重放攻击）
////		long currentTime = System.currentTimeMillis();
////		if (Math.abs(currentTime - header.getTimestamp()) > SIGN_EXPIRE_TIME) {
////			return false;
////		}
////
////		// 2. 保存原始签名并置空，避免参与签名计算
////		String originalSign = header.getSign();
////		header.setSign(null);
////
////		// 3. 验签
////		boolean verifyResult = verifySign(request, originalSign, publicKey);
////
////		// 4. 恢复签名
////		header.setSign(originalSign);
////
////		return verifyResult;
////	}
//
//	/**
//	 * 为响应生成签名
//	 * 
//	 * @param response   响应对象
//	 * @param privateKey 私钥
//	 * @return 签名后的响应对象
//	 */
//	public static <T> ApiResponse<T> signResponse(ApiResponse<T> response, String privateKey) {
//		Assert.notNull(response, "响应对象不能为空");
//		Assert.notNull(response.getResponseHeader(), "响应头不能为空");
//
//		ResponseHeader header = response.getResponseHeader();
//
//		// 1. 设置响应时间戳
//		header.setTimestamp(System.currentTimeMillis());
//
//		// 2. 保存原始签名并置空
//		header.setSign(null);
//
//		// 3. 生成签名
//		String sign = generateSign(response, privateKey);
//
//		// 4. 设置签名
//		header.setSign(sign);
//
//		return response;
//	}
//
//	/**
//	 * 将对象转为按key排序的JSON字符串（确保签名的唯一性）
//	 */
//	private static String getSortedJsonString(Object obj) {
//		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
//		return sortJson(jsonObject).toJSONString();
//	}
//
//	/**
//	 * 递归排序JSON对象的key
//	 */
//	private static JSONObject sortJson(JSONObject jsonObject) {
//		JSONObject sortedJson = new JSONObject(true);
//		TreeMap<String, Object> sortedMap = new TreeMap<>(jsonObject);
//
//		for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
//			String key = entry.getKey();
//			Object value = entry.getValue();
//
//			if (value instanceof JSONObject) {
//				sortedJson.put(key, sortJson((JSONObject) value));
//			} else if (value instanceof List) {
//				sortedJson.put(key, sortJsonArray((List<?>) value));
//			} else {
//				sortedJson.put(key, value);
//			}
//		}
//
//		return sortedJson;
//	}
//
//	/**
//	 * 排序JSON数组
//	 */
//	private static List<Object> sortJsonArray(List<?> list) {
//		List<Object> sortedList = new ArrayList<>();
//
//		for (Object obj : list) {
//			if (obj instanceof JSONObject) {
//				sortedList.add(sortJson((JSONObject) obj));
//			} else if (obj instanceof List) {
//				sortedList.add(sortJsonArray((List<?>) obj));
//			} else {
//				sortedList.add(obj);
//			}
//		}
//
//		return sortedList;
//	}
//}