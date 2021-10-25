package com.fib.upp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fib.commons.security.SecurityEncryptor;
import com.fib.commons.util.CommUtils;
import com.fib.commons.web.ResultRsp;
import com.fib.commons.web.ResultUtil;
import com.fib.core.base.service.ISecurityService;
import com.fib.core.util.ConstantUtil;
import com.fib.core.util.SpringContextUtils;
import com.fib.core.util.StatusCode;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebCtrlerTest {
	private Logger logger = LoggerFactory.getLogger(WebCtrlerTest.class);

	@Test
	public void testMe() {
		String systemCode = "100001";
		Map<String, String> data = new HashMap<>();
		data.put("systemCode", systemCode);
		data.put("transactionId", IdUtil.simpleUUID());
		ResultRsp<Object> body = ResultUtil.message(StatusCode.SUCCESS, data);
		String content = JSONUtil.toJsonStr(body);

		/* 1.对原始数据取摘要-摘要加密算法SM3 */
		String contentHash = SmUtil.sm3(content);
		logger.info("content={},contentHash={}", content, contentHash);

		/* 2.用自己私钥对摘要签名-SM2 */
		ISecurityService securityService = SpringContextUtils.getBean("securityService");
		String ownPrivateKey = securityService.queryPrivateKey(ConstantUtil.OTHER_SYSTEM_CODE);
		byte[] signedContext = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.sign(StrUtil.bytes(contentHash, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(ownPrivateKey));
		logger.info("authentication={}", HexUtil.encodeHexStr(signedContext));

		/* 3.用对方公钥对非对称密钥加密-SM2 */
		String securityKeySource = CommUtils.getRandom(16);
		logger.info("securityKeySource={}", securityKeySource);
		String otherPublicKey = securityService.queryPublicKey(ConstantUtil.UPP_SYSTEM_CODE);
		byte[] cipherTxt = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2").encrypt(
				StrUtil.bytes(securityKeySource, CharsetUtil.CHARSET_UTF_8), SecureUtil.decode(otherPublicKey));
		logger.info("securityKey={}", HexUtil.encodeHexStr(cipherTxt));

		/* 4.用对称加密算法对报文内容加密-SM4 */
		byte[] encryptedBodyContent = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null)
				.getExtension("SM4").encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8),
						securityKeySource.getBytes(CharsetUtil.CHARSET_UTF_8));
		logger.info("encryptedBodyContent={}", HexUtil.encodeHexStr(encryptedBodyContent));
		String dateTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN);
		logger.info("TimeStamp={}", dateTime);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("authentication", HexUtil.encodeHexStr(signedContext));
		headers.set("securityKey", HexUtil.encodeHexStr(cipherTxt));
		headers.set("systemCode", systemCode);
		headers.set("timeStamp", dateTime);

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("body", HexUtil.encodeHexStr(encryptedBodyContent));

		String url = "http://localhost:8080/upp/messageIn/test";
		RestTemplate restTemplate = new RestTemplate();
		String res = restTemplate.postForObject(url, new HttpEntity<>(params, headers), String.class);
		logger.info("RESTFul return response: {}", res);
	}
}
