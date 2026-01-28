package com.fib.mall.message;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

public class Test {

	public static void main(String[] args) {
		ApiRequest<Object> req = new ApiRequest<>();

		RequestHeader reqHeader = new RequestHeader();
		reqHeader.setAppId("app01");
		reqHeader.setTimestamp(System.currentTimeMillis());
		reqHeader.setNonce(RandomUtil.randomString(16));
		reqHeader.setRequestId(IdUtil.getSnowflakeNextIdStr());

		req.setRequestHeader(reqHeader);

		RequestBody<Object> reqBody = new RequestBody<>();
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("pame", "fangyh");
		retMap.put("age", 11);
		retMap.put("12312", 11);
		retMap.put("namge", 11);
		retMap.put("gege", 11);
		reqBody.setBizData(retMap);
		IO.println(reqBody.getBizData());

		Object body = reqBody.getBizData();
		String[] aesKeyIv = EncryptUtils.generateAesKeyIv();
		String aesKey = aesKeyIv[0];
		String aesIv = aesKeyIv[1];

		String bodyStr = SingletonObjectMapper.toSortedJson(body);
		IO.println("bodyStr=" + bodyStr);
		String encryedBody = EncryptUtils.aesEncrypt(bodyStr, aesKey, aesIv);
		IO.println(encryedBody);

		String algorithm = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
		// 生成RSA密钥对
		KeyPair keyPair = SecureUtil.generateKeyPair(algorithm);
		// 获取私钥和公钥
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		String privateKeyStr = Base64.encode(privateKey.getEncoded());
		String publicKeyStr = Base64.encode(publicKey.getEncoded());

		IO.println("私钥: " + privateKeyStr);
		IO.println("公钥: " + publicKeyStr);

		RSA rsa = new RSA("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", null, publicKeyStr);
		// String encrypedRsa = EncryptSignUtils.rsaEncryptAesKey(aesKey, aesIv,
		// encryedBody);
		String keyIvStr = StrUtil.format("{}:{}", aesKey, aesIv);
		IO.println("keyIvStr=" + keyIvStr);
		String encrypedRsa = rsa.encryptBase64(keyIvStr, CharsetUtil.CHARSET_UTF_8, KeyType.PublicKey);
		IO.println("encrypedRsa=" + encrypedRsa);

		rsa = new RSA("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", privateKeyStr, null);
		String decryRsa = rsa.decryptStr(encrypedRsa, KeyType.PrivateKey, CharsetUtil.CHARSET_UTF_8);
		IO.println("decryRsa=" + decryRsa);

		reqHeader.setEncryptedAesKey(encrypedRsa);

		// appId + timestamp + nonce + cipherText
		String aa = reqHeader.getAppId() + reqHeader.getTimestamp() + reqHeader.getNonce() + bodyStr;
		String sign = Base64.encode(SignUtil.sign(SignAlgorithm.SHA256withRSA, privateKeyStr, null).sign(aa));
		reqHeader.setSign(sign);

		reqBody.setEncryptedBody(encryedBody);
		req.setRequestBody(reqBody);
		String jsonStr = SingletonObjectMapper.toSortedJson(req);
		IO.println(jsonStr);

		// 解密

		ApiRequest<?> apiReq = SingletonObjectMapper.fromJson(jsonStr, ApiRequest.class);
		String reqEncrypt = apiReq.getRequestBody().getEncryptedBody();
		IO.println("reqEncrypt=" + reqEncrypt);

		String keyvStr = apiReq.getRequestHeader().getEncryptedAesKey();
		rsa = new RSA("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", privateKeyStr, null);
		String reqKeySrc = rsa.decryptStr(keyvStr, KeyType.PrivateKey, CharsetUtil.CHARSET_UTF_8);
		IO.println("reqKeySrc=" + reqKeySrc);
		String[] ivStr = StrUtil.splitToArray(reqKeySrc, ":");
		String bodySrc = EncryptUtils.aesDecrypt(reqEncrypt, ivStr[0], ivStr[1]);
		IO.println("bodySrc=" + bodySrc);

		// appId + timestamp + nonce + cipherText
		RequestHeader rh = apiReq.getRequestHeader();
		String str = rh.getAppId() + rh.getTimestamp() + rh.getNonce() + bodySrc;
		boolean verify = SignUtil.sign(SignAlgorithm.SHA256withRSA, null, publicKeyStr).verify(str.getBytes(),
				Base64.decode(apiReq.getRequestHeader().getSign()));
		IO.print("verify=" + verify);
//		Map<?, ?> ret = EncryptSignUtils.aesDecrypt(encryedBody, aesKey, aesIv, Map.class);
//		IO.println(ret);
	}
}
