package com.fib.core.advice.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import com.fib.commons.security.SecurityEncryptor;
import com.fib.core.base.service.ISecurityService;
import com.fib.core.util.ConstantUtil;
import com.fib.core.util.SpringContextUtils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-10-19
 */
public class DecryptHttpInputMessage implements HttpInputMessage {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private HttpHeaders headers;
	private InputStream body;

	public DecryptHttpInputMessage(HttpInputMessage inputMessage, String charset, boolean showLog) throws Exception {

		this.headers = inputMessage.getHeaders();
		String content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines()
				.collect(Collectors.joining(System.lineSeparator()));
		String authentication = headers.getFirst("authentication");
		String securityKey = headers.getFirst("securityKey");
		String systemCode = headers.getFirst("systemCode");
		if (StrUtil.isEmpty(authentication) || StrUtil.isEmpty(securityKey) || StrUtil.isEmpty(systemCode)) {
			// TODO
		}

		ISecurityService securityService = SpringContextUtils.getBean("securityService");

		String ownPrivateKey = securityService.queryPrivateKey(ConstantUtil.UPP_SYSTEM_CODE);
		if (StrUtil.isEmpty(ownPrivateKey)) {
			// TODO
		}

		String otherpublicKey = securityService.queryPublicKey(systemCode);
		if (StrUtil.isEmpty(otherpublicKey)) {
			// TODO
		}

		/* 1.用自己私钥对非对称密钥解密-SM2 */
		byte[] encryptedKey = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.decrypt(HexUtil.decodeHex(securityKey), SecureUtil.decode(ownPrivateKey));
		log.info("securityKey={}", StrUtil.str(encryptedKey, CharsetUtil.CHARSET_UTF_8));

		/* 2.用对称加密算法对报文内容解密-SM4 */
		if (!JSONUtil.isJson(content)) {
			// TODO
		}

		JSONObject jsonCxt = JSONUtil.parseObj(content);
		JSONArray jsonBody = jsonCxt.getJSONArray("body");
		String encryptedBody = (String) jsonBody.get(0);
		log.info("encryptedBody={}", encryptedBody);
		byte[] body = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4")
				.decrypt(HexUtil.decodeHex(encryptedBody), encryptedKey);

		String bodySource = StrUtil.str(body, CharsetUtil.CHARSET_UTF_8);
		log.info("bodySource={}", bodySource);

		JSONObject messageBody = JSONUtil.parseObj(bodySource);
		String rspCode = messageBody.getStr("rspCode");
		if (!StrUtil.equals("000000", rspCode)) {
			// TODO
		}

		Object data = messageBody.getObj("rspObj");

		/* 3.对原始数据取摘要-摘要加密算法SM3 */
		String bodyHash = SmUtil.sm3(bodySource);
		log.info("bodyHash={}", bodyHash);

		/* 4.用对方公钥对摘要验签-SM2 */
		boolean verifyFlag = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.verify(StrUtil.bytes(bodyHash, CharsetUtil.CHARSET_UTF_8), HexUtil.decodeHex(authentication),
						SecureUtil.decode(otherpublicKey));
		log.info("verifyFlag=" + verifyFlag);
		if (!verifyFlag) {
			// TODO
		}
		this.body = new ByteArrayInputStream(JSONUtil.toJsonStr(data).getBytes());
	}

	@Override
	public InputStream getBody() {
		return body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}
}
