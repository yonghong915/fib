package com.fib.autoconfigure.crypto.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.dubbo.rpc.model.ScopeModelUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.autoconfigure.crypto.annotation.AlgorithmType.Algorithm;
import com.fib.autoconfigure.crypto.dto.Request;
import com.fib.autoconfigure.crypto.dto.RequestHeader;
import com.fib.autoconfigure.crypto.security.SecurityEncryptor;
import com.fib.autoconfigure.crypto.service.ISecurityService;
import com.fib.core.util.ConstantUtil;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;

/**
 * 安全服务实现
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-31 14:43:33
 */
public class SecurityServiceImpl implements ISecurityService {

	@Override
	public String queryPrivateKey(String systemCode) {
		String privateKey = "";
		if (ConstantUtil.UPP_SYSTEM_CODE.equals(systemCode)) {
			privateKey = "308193020100301306072a8648ce3d020106082a811ccf5501822d04793077020101042083e754ffc0a63d29ce57ab647d436a4c345d9c93197707e25feec67996e7fe67a00a06082a811ccf5501822da14403420004bdcaf7042b189d2df58de8b5c166315f7fcd2bc72373c22dad0bc5918fffc50ee94df836e23b7d9472047110c33f2d662b92fab65ffd40db8f6f5124df90033b";
		} else if ("100001".equals(systemCode)) {
			privateKey = "308193020100301306072a8648ce3d020106082a811ccf5501822d0479307702010104200f39dc93fddc5109e66ede7e913da4a706579fe7d02490f164d3d50246c3aefda00a06082a811ccf5501822da144034200042d4eed9818095f0b7ae96610f1bf1285f36ff85998054ad3d66366ce35a06c000c54a90e52de821ec53c502d0f66b2f8cdfb8ecade74bd2a00864a1d6c520908";
		}
		return privateKey;
	}

	@Override
	public String queryPublicKey(String systemCode) {
		String publicKey = "";
		if (ConstantUtil.UPP_SYSTEM_CODE.equals(systemCode)) {
			publicKey = "3059301306072a8648ce3d020106082a811ccf5501822d03420004bdcaf7042b189d2df58de8b5c166315f7fcd2bc72373c22dad0bc5918fffc50ee94df836e23b7d9472047110c33f2d662b92fab65ffd40db8f6f5124df90033b";
		} else if ("100001".equals(systemCode)) {
			publicKey = "3059301306072a8648ce3d020106082a811ccf5501822d034200042d4eed9818095f0b7ae96610f1bf1285f36ff85998054ad3d66366ce35a06c000c54a90e52de821ec53c502d0f66b2f8cdfb8ecade74bd2a00864a1d6c520908";
		}
		return publicKey;
	}

	@Override
	public long getCurrTimestamp() {
		LocalDateTime localDateTime = LocalDateTimeUtil.now();
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	@Override
	public String getNonce(long timestamp) {
		return NetUtil.getLocalhostStr() + "|" + timestamp + "|" + RandomUtil.randomString(8);
	}

	@Override
	public Request buildReq(Object body, Algorithm algorithm) {
		Request request = new Request();
		RequestHeader reqHeader = new RequestHeader();

		long timestamp = getCurrTimestamp();
		String nonce = getNonce(timestamp);
		String securityKey = RandomUtil.randomString(16);
		reqHeader.setTimestamp(timestamp);
		reqHeader.setNonce(SmUtil.sm3(nonce));
		reqHeader.setSecurityKey(securityKey);
		reqHeader.setSystemCode(ConstantUtil.UPP_SYSTEM_CODE);

		ObjectMapper objectMapper = new ObjectMapper();
		String bodyStr = "";
		try {
			bodyStr = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2");

		byte[] text = (reqHeader.getTimestamp() + reqHeader.getNonce() + bodyStr).getBytes(CharsetUtil.CHARSET_UTF_8);
		byte[] ss = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension(algorithm.getValue()).encrypt(text,
				securityKey.getBytes(CharsetUtil.CHARSET_UTF_8));

		String contentHash = HexUtil.encodeHexStr(ss);
		reqHeader.setAuthentication(contentHash);

		request.setRequestHeader(reqHeader);
		return request;
	}

	public String getEncrypBody(Object body, RequestHeader reqHeader) {
		/* 用对称加密算法对报文内容加密-SM4 */
		String bodyStr = JSONUtil.toJsonStr(body);
		byte[] encryptedBodyContent = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4")
				.encrypt(CharSequenceUtil.bytes(bodyStr, CharsetUtil.CHARSET_UTF_8), reqHeader.getSecurityKey().getBytes(CharsetUtil.CHARSET_UTF_8));
		String bodyHash = HexUtil.encodeHexStr(encryptedBodyContent);
		// logger.info("encryptedBody={}", bodyHash);
		return bodyHash;
	}

	public String getSecurityKey(RequestHeader reqHeader) {
		/* 3.用对方公钥对对称密钥加密-SM2 */
		String pubKey = queryPublicKey(ConstantUtil.UPP_SYSTEM_CODE);
		byte[] cipherTxt = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.encrypt(CharSequenceUtil.bytes(reqHeader.getSecurityKey(), CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(pubKey));
		String securityKey = HexUtil.encodeHexStr(cipherTxt);

		return securityKey;
	}
}
