package com.fib.uqcp.test;

import java.io.File;
import java.io.FileReader;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.autoconfigure.crypto.security.SecurityEncryptor;
import com.fib.uqcp.api.msg.RequestHeader;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;

public class TestEncry {
	private Logger logger = LoggerFactory.getLogger(TestEncry.class);

	public static void main(String[] args) {
		TestEncry d = new TestEncry();
		d.test();
	}

	public void test() {
		RequestHeader reqHeader = new RequestHeader();

		LocalDateTime localDateTime = LocalDateTimeUtil.now();
		long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		reqHeader.setTimestamp(timestamp);
		reqHeader.setSystemCode("FIB");

		// ip+时间+随机数
		String nonce = NetUtil.getLocalhostStr() + "|" + timestamp + "|" + RandomUtil.randomString(8);
		System.out.println(nonce);

		String securityKey = RandomUtil.randomString(16);

		reqHeader.setNonce(SmUtil.sm3(nonce));
		reqHeader.setAuthentication(null);
		reqHeader.setSecurityKey(securityKey);

		Map<String, Object> body = new HashMap<>();
		body.put("aaa", 1111);
		body.put("ccc", 3333);
		body.put("bbb", 222);

		String bodyStr = JSONUtil.toJsonStr(body);
		logger.info("bodyStr={}", bodyStr);

		/* 1.对原始数据取摘要-摘要加密算法SM3 =SM3(timestamp+nonce+bodyStr) */
		String contentHash = SmUtil.sm3(reqHeader.getTimestamp() + reqHeader.getNonce() + bodyStr);

		reqHeader.setAuthentication(contentHash);

		/* 用对称加密算法对报文内容加密-SM4 */
		byte[] encryptedBodyContent = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4")
				.encrypt(CharSequenceUtil.bytes(bodyStr, CharsetUtil.CHARSET_UTF_8), reqHeader.getSecurityKey().getBytes(CharsetUtil.CHARSET_UTF_8));
		String bodyHash = HexUtil.encodeHexStr(encryptedBodyContent);
		logger.info("encryptedBody={}", bodyHash);

		/**
		 * TODO 生成公私钥对
		 */
		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		byte[] privateKey = pair.getPrivate().getEncoded();
		byte[] publicKey = pair.getPublic().getEncoded();

		// 读取pem格式的私钥
		// public PrivateKey readPemPrivateKey(String pemFilePath) throws Exception {
		// 使用Bouncy Castle提供的PEMReader类来读取pem文件
		byte[] privKey = null;// Base64.decode(FileUtil.readBytes("D://private_key.pem"));
		try (PemReader pemReader = new PemReader(new FileReader("D://sub_private_key.pem"))) {
			PemObject pemObject = pemReader.readPemObject();

			// 获取pem对象中的私钥字节数组
			byte[] pemContent = pemObject.getContent();
			privKey = pemContent;
//			// 使用KeyFactory将私钥字节数组转换为私钥对象
//			KeyFactory keyFactory = KeyFactory.getInstance("SM3");
//			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemContent);
//			keyFactory.ge
			// return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
		}
		// }

		X509Certificate cer = (X509Certificate) SecureUtil.readX509Certificate(FileUtil.getInputStream(new File("D:\\sub_public_key.pem")));
		byte[] publKey = null;
		try {
			publKey = cer.getEncoded();
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cer.getType());
		System.out.println(cer.getNotBefore());
		System.out.println(cer.getNotAfter());
		/* 3.用对方公钥对对称密钥加密-SM2 */
		byte[] cipherTxt = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.encrypt(CharSequenceUtil.bytes(reqHeader.getSecurityKey(), CharsetUtil.CHARSET_UTF_8), cer.getPublicKey().getEncoded());
		securityKey = HexUtil.encodeHexStr(cipherTxt);

		reqHeader.setSecurityKey(securityKey);
		logger.info("reqHeader={}", JSONUtil.toJsonStr(reqHeader));
		/**
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		// 获取请求处理
		timestamp = reqHeader.getTimestamp();
		securityKey = reqHeader.getSecurityKey();
		String bodyS = bodyHash;
		String authentication = reqHeader.getAuthentication();
		nonce = reqHeader.getNonce();
		String sysCode = reqHeader.getSystemCode();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* 校验是否超时 */
		localDateTime = LocalDateTimeUtil.now();
		long currTimestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		if (currTimestamp - timestamp > 5000) {
			System.out.println("timeout");
		} else {
			System.out.println("no timeout");
		}

		/* 3.用已方私钥对对称密钥解密-SM2 */
		byte[] encryptedKey = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM2")
				.decrypt(HexUtil.decodeHex(securityKey), privKey);
		logger.info("securityKey={}", StrUtil.str(encryptedKey, CharsetUtil.CHARSET_UTF_8));

		byte[] msgBody = ScopeModelUtil.getExtensionLoader(SecurityEncryptor.class, null).getExtension("SM4").decrypt(HexUtil.decodeHex(bodyS),
				encryptedKey);
		String bodySource = StrUtil.str(msgBody, CharsetUtil.CHARSET_UTF_8);
		logger.info("bodySource={}", bodySource);

		System.out.println(timestamp + " " + nonce + " " + bodySource);
		contentHash = SmUtil.sm3(timestamp + nonce + bodySource);
		logger.info("contentHash={}", contentHash);

		if (authentication.equals(contentHash)) {
			System.out.println("验签成功");
		}
	}
}
