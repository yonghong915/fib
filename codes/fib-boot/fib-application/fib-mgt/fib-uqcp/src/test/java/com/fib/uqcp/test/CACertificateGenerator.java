package com.fib.uqcp.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;

public class CACertificateGenerator {
	public static X509Certificate generateCACertificate(PrivateKey caPrivateKey, PublicKey caPublicKey) throws NoSuchAlgorithmException,
			OperatorCreationException, CertificateException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		// 构建证书请求
		PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(new X500Principal("CN=CA"), caPublicKey);

		// 用CA私钥签署证书请求
		JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SM3WITHSM2");
		ContentSigner signer = signerBuilder.build(caPrivateKey);
		PKCS10CertificationRequest request = requestBuilder.build(signer);

		// 构建证书
		X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(new X500Name("CN=CA"), // 颁发者
				BigInteger.ONE, // 序列号
				new Date(System.currentTimeMillis() - 10000), // 生效日期
				new Date(System.currentTimeMillis() + 86400000L * 365), // 过期日期
				request.getSubject(), // 主题
				request.getSubjectPublicKeyInfo() // 公钥信息
		);

		// 使用CA私钥签署证书
		X509CertificateHolder certificateHolder = certificateBuilder.build(signer);
		X509Certificate caCertificate = new JcaX509CertificateConverter().getCertificate(certificateHolder);
		return caCertificate;
	}

	/**
	 * 生成自签名的PEM格式的证书和私钥文件
	 */
	public static void main(String[] args) throws OperatorCreationException, IOException {
		// 生成主题信息
		X500Name subject = generateSubject("CN", "Beijing", "Beijing", "", "", "www.test.com");
		// 生成RSA密钥对
		KeyPair keyPair = SecureUtil.generateKeyPair("SM2", 2048);
		assert keyPair != null;
		PublicKey aPublic = keyPair.getPublic();
		PrivateKey aPrivate = keyPair.getPrivate();
		// 下面是私钥key生成的过程
		byte[] privateKeyEncode = aPrivate.getEncoded();
		String privateKeyStr = Base64.encode(privateKeyEncode);
		// String privateKeyFileContent = "" + "-----BEGIN PRIVATE KEY-----\n" +
		// lf(privateKeyStr, 64) + "-----END PRIVATE KEY-----";

		// FileUtil.writeUtf8String(privateKeyFileContent, new
		// File("D:\\private_key.pem"));

		try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("D:\\private_key.pem"));) {
			pemWriter.writeObject(aPrivate);
		}
		System.out.println(Base64.encode(aPublic.getEncoded()));

		// 下面是PEM格式的证书生成过程
		long currTimestamp = System.currentTimeMillis();
		X500Name issuer = subject;
		X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(issuer, BigInteger.valueOf(System.currentTimeMillis()),
				new Date(currTimestamp), new Date(currTimestamp + (long) 365 * 24 * 60 * 60 * 1000), subject, aPublic);
		JcaContentSignerBuilder sha256WITHRSA = new JcaContentSignerBuilder("SM3WITHSM2");// SHA256WITHSM2 SHA256WITHRSA SM3WITHSM2
		ContentSigner contentSigner = sha256WITHRSA.build(aPrivate);
		X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
		org.bouncycastle.asn1.x509.Certificate certificate = x509CertificateHolder.toASN1Structure();
		byte[] encoded = certificate.getEncoded();
		String certStr = Base64.encode(encoded);
		String certFileContent = "" + "-----BEGIN CERTIFICATE-----\n" + lf(certStr, 64) + "-----END CERTIFICATE-----";
		FileUtil.writeUtf8String(certFileContent, new File("D:\\public_key.pem"));

		X509Certificate cer = (X509Certificate) SecureUtil.readX509Certificate(FileUtil.getInputStream(new File("D:\\public_key.pem")));
		System.out.println(cer.getType());
		System.out.println(cer.getNotBefore());
		System.out.println(cer.getNotAfter());

		try {
			System.out.println(Base64.encode(cer.getEncoded()));
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 生成Subject信息
	 *
	 * @param C  Country Name (国家代号),eg: CN
	 * @param ST State or Province Name (洲或者省份),eg: Beijing
	 * @param L  Locality Name (城市名),eg: Beijing
	 * @param O  Organization Name (可以是公司名称),eg: 北京创新乐知网络技术有限公司
	 * @param OU Organizational Unit Name (可以是单位部门名称)
	 * @param CN Common Name (服务器ip或者域名),eg: 192.168.30.71 or www.baidu.com
	 * @return X500Name Subject
	 */
	public static X500Name generateSubject(String C, String ST, String L, String O, String OU, String CN) {
		X500NameBuilder x500NameBuilder = new X500NameBuilder();
		x500NameBuilder.addRDN(BCStyle.C, C);
		x500NameBuilder.addRDN(BCStyle.ST, ST);
		x500NameBuilder.addRDN(BCStyle.L, L);
		x500NameBuilder.addRDN(BCStyle.O, O);
		x500NameBuilder.addRDN(BCStyle.OU, OU);
		x500NameBuilder.addRDN(BCStyle.CN, CN);
		return x500NameBuilder.build();
	}

	public static KeyPair generateRsaKeyPair(int keySize) {
		try {
			KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
			rsa.initialize(keySize);
			return rsa.generateKeyPair();
		} catch (NoSuchAlgorithmException ignore) {
		}
		return null;
	}

	public static String lf(String str, int lineLength) {
		assert str != null;
		assert lineLength > 0;
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		int n = 0;
		for (char aChar : chars) {
			sb.append(aChar);
			n++;
			if (n == lineLength) {
				n = 0;
				sb.append("\n");
			}
		}
		if (n != 0)
			sb.append("\n");
		return sb.toString();
	}
}
