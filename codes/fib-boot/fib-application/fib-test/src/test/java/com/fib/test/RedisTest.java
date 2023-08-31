//package com.fib.test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.fib.commons.security.RequestHeader;
//import com.fib.commons.security.SecurityEncryptor;
//import com.fib.commons.security.support.SM2Encryptor;
//import com.fib.commons.security.support.SM4Encryptor;
//import com.fib.encrypt.RequestSec;
//
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.hutool.core.util.CharsetUtil;
//import cn.hutool.core.util.HexUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.BCUtil;
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.crypto.SmUtil;
//import cn.hutool.crypto.asymmetric.SM2;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class RedisTest {
//	private static final Logger logger = LoggerFactory.getLogger(RequestSec.class);
//	private static final String NONCE_KEY = "x-nonce-";
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
//
//	@Test
//	public void testString() {
//		redisTemplate.opsForValue().set("city123", "beijing");
//
//		String value = (String) redisTemplate.opsForValue().get("city123");
//		System.out.println(value);
//
//		redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);
//
//		Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
//		System.out.println(aBoolean);
//
//		SM2 sm2 = SmUtil.sm2();
//		byte[] privateKey = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
//		byte[] publicKey = BCUtil.encodeECPublicKey(sm2.getPublicKey());
//		String priKey = HexUtil.encodeHexStr(privateKey);
//		String pubKey = HexUtil.encodeHexStr(publicKey);
//		String ownPrivateKey = priKey;
//		String otherPublicKey = pubKey;
//		// TODO
//		logger.info("priKey={}", priKey);
//		logger.info("pubKey={}", pubKey);
//
//		logger.info("---------------请求组装开始----------------------");
//		// 对称加密Key
//		String securityKeySource = getRandom(16);
//		logger.info("securityKeySource={}", securityKeySource);
//
//		SecurityEncryptor encryptor = new SM2Encryptor();
//		byte[] cipherTxt = encryptor.encrypt(CharSequenceUtil.bytes(securityKeySource, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(otherPublicKey));
//		String securityKey = HexUtil.encodeHexStr(cipherTxt);
//		logger.info("securityKey={}", securityKey);
//
//		// 对原始数据取摘要-摘要加密算法SM3
//		Map<String, Object> rsp = new HashMap<>();
//
//		Map<String, Object> body = new HashMap<>();
//		body.put("name", "zhangsan");
//		body.put("age", 20);
//
//		long timeStamp = System.currentTimeMillis();
//		logger.info("timeStamp={}", timeStamp);
//		String nonce = getRandom(16);
//		logger.info("nonce={}", nonce);
//		String systemCode = "UPP";
//		logger.info("systemCode={}", systemCode);
//
//		// x-Nonce X-Time X-Sign
//		RequestHeader header = RequestHeader.builder().systemCode(systemCode).timeStamp(timeStamp).nonce(nonce).build();
//
//		rsp.put("body", body);
//		rsp.put("header", header);
//		rsp.put("rspCode", "000000");
//		rsp.put("rspMsg", "success");
//
//		String content = JSONUtil.toJsonStr(rsp);
//		String contentHash = SmUtil.sm3(content);
//		logger.info("content={},contentHash={}", content, contentHash);
//
//		// 对原始数据摘要签名
//		byte[] signedContext = encryptor.sign(CharSequenceUtil.bytes(contentHash, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(ownPrivateKey));
//		String authentication = HexUtil.encodeHexStr(signedContext);
//		logger.info("authentication={}", authentication);
//
//		/* 4.用对称加密算法对报文内容加密-SM4 */
//		encryptor = new SM4Encryptor();
//		byte[] encryptedBodyContent = encryptor.encrypt(CharSequenceUtil.bytes(content, CharsetUtil.CHARSET_UTF_8),
//				securityKeySource.getBytes(CharsetUtil.CHARSET_UTF_8));
//
//		String bodyHash = HexUtil.encodeHexStr(encryptedBodyContent);
//		logger.info("encryptedBodyContent={}", bodyHash);
//
//		// 报文头 securityKey authentication timeStamp nonce systemCode
//		// 报文体 bodyHash
//		logger.info("---------------请求组装结束----------------------");
//
//		logger.info("---------------返回解析开始----------------------");
//		// authentication timeStamp nonce systemCode
//
//		/* 1.用自己私钥对非对称密钥解密-SM2 */
//		encryptor = new SM2Encryptor();
//		byte[] encryptedKey = encryptor.decrypt(HexUtil.decodeHex(securityKey), SecureUtil.decode(ownPrivateKey));
//		securityKeySource = StrUtil.str(encryptedKey, CharsetUtil.CHARSET_UTF_8);
//		logger.info("securityKeySource={}", securityKeySource);
//
//		encryptor = new SM4Encryptor();
//		byte[] msgBody = encryptor.decrypt(HexUtil.decodeHex(bodyHash), encryptedKey);
//
//		String bodySource = StrUtil.str(msgBody, CharsetUtil.CHARSET_UTF_8);
//		logger.info("bodySource={}", bodySource);
//		contentHash = SmUtil.sm3(bodySource);
////		JSONObject jsonCxt = JSONUtil.parseObj(bodyHash);
////		JSONArray jsonBody = jsonCxt.getJSONArray("body");
////		String encryptedBody = (String) jsonBody.get(0);
////		logger.info("encryptedBody={}", encryptedBody);
////		encryptor = new SM4Encryptor();
////		byte[] msgBody = encryptor.decrypt(HexUtil.decodeHex(encryptedBody), encryptedKey);
//		encryptor = new SM2Encryptor();
//		boolean verifyFlag = encryptor.verify(CharSequenceUtil.bytes(contentHash, CharsetUtil.CHARSET_UTF_8), HexUtil.decodeHex(authentication),
//				SecureUtil.decode(otherPublicKey));
//		logger.info("verifyFlag={}", verifyFlag);
//
//		if (!verifyFlag) {
//			logger.info("Failed to verify data sign.");
//			return;
//		}
//
//		/*
//		 * 1.重放验证 判断timestamp时间戳与当前时间是否操过60s（过期时间根据业务情况设置）,如果超过了就提示签名过期。
//		 */
//		long signMaxTime = 60000;
//		JSONObject jsonCxt = JSONUtil.parseObj(bodySource);
//		JSONObject requestHeader = jsonCxt.getJSONObject("header");
//		// JSONUtil.toBean(jsonCxt, RequestHeader.class);
//		long now = System.currentTimeMillis();
//		long reqT = requestHeader.getLong("timeStamp");
//		if (now - reqT > signMaxTime) {// 只能在60s内进行重放攻击
//			logger.info("-----------签名过期-------------");
//			return;
//		}
//		// 2. 判断nonce redist的key 设置过期时间--重复请求
//		nonce = requestHeader.getStr("nonce");
//		String nonceKey = NONCE_KEY + nonce + timeStamp;
//		logger.info("nonceKey={}", nonceKey);
//		boolean existsFlag = redisTemplate.hasKey("x-nonce-HZ8GuWU44ttTLHDk1676018305104");
//		if (existsFlag) {// 确保接口只能被调用一次，可以很好的解决重放攻击问题
//			// 请求重复
//			logger.info("-----------请求重复-------------");
//			return;
//		} else {
//			redisTemplate.opsForValue().set(nonceKey, nonce + timeStamp, signMaxTime, TimeUnit.MILLISECONDS);
//			logger.info("设置redis数据nonceKey={}", nonceKey);
//		}
//
//		logger.info("---------------返回解析结束----------------------");
//	}
//
//	public static String getRandom(int len) {
//		String source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		StringBuilder rs = new StringBuilder();
//		int sourceLen = source.length();
//		for (int i = 0; i < len; i++) {
//			int a = ThreadLocalRandom.current().nextInt(sourceLen);
//			rs.append(source.charAt(a));
//		}
//		return rs.toString();
//	}
//}
