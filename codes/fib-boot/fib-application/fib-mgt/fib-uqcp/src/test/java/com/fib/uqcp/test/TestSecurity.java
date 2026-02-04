package com.fib.uqcp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcECContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;

public class TestSecurity {

	public static void main(String[] args) {
		// 生成RSA密钥对
		KeyPair keyPair = SecureUtil.generateKeyPair("SM2", 2048);

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		String privateKeyStr = Base64.encode(privateKey.getEncoded());
		String privateKeyFileContent = "" + "-----BEGIN PRIVATE KEY-----\n" + lf(privateKeyStr, 64) + "-----END PRIVATE KEY-----";

		FileUtil.writeUtf8String(privateKeyFileContent, new File("D:\\sub_private_key.pem"));

		try {
			PKCS10CertificationRequest csr = generateCSR(privateKey, publicKey);
			X509Certificate certificate = generateCACertificate(csr, "D://public_key.pem", privateKey);

			byte[] encoded = certificate.getEncoded();
			String certStr = Base64.encode(encoded);
			String certFileContent = "" + "-----BEGIN CERTIFICATE-----\n" + lf(certStr, 64) + "-----END CERTIFICATE-----";
			FileUtil.writeUtf8String(certFileContent, new File("D:\\sub_public_key.pem"));
		} catch (InvalidKeyException | NoSuchAlgorithmException | OperatorCreationException | CertificateException | NoSuchProviderException
				| SignatureException | IOException e) {
			e.printStackTrace();
		}
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

	/**
	 * 用户首先产生自己的密钥对，并将公共密钥及部分个人信息传送给CA（通过P10请求）
	 * 
	 * @param caPrivateKey
	 * @param caPublicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws OperatorCreationException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static PKCS10CertificationRequest generateCSR(PrivateKey caPrivateKey, PublicKey caPublicKey) throws NoSuchAlgorithmException,
			OperatorCreationException, CertificateException, NoSuchProviderException, InvalidKeyException, SignatureException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		/* 证书使用者信息 */
		X500Name subject = generateSubject("CN", "SiChuan", "ChengDu", "FIB", "dev", "www.fib.com");
		// 构建证书请求
		PKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(subject, caPublicKey);

		// 用CA私钥签署证书请求
		JcaContentSignerBuilder signerBuilder = new JcaContentSignerBuilder("SM3WITHSM2");
		ContentSigner signer = signerBuilder.build(caPrivateKey);
		PKCS10CertificationRequest csr = requestBuilder.build(signer);
		return csr;
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

	/**
	 * CA接受请求并生成证书
	 * 
	 * @param csr
	 * @param certPath
	 * @param caPrivateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws OperatorCreationException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws IOException
	 */
	public static X509Certificate generateCACertificate(PKCS10CertificationRequest csr, String certPath, PrivateKey caPrivateKey)
			throws NoSuchAlgorithmException, OperatorCreationException, CertificateException, NoSuchProviderException, InvalidKeyException,
			SignatureException, IOException {
		// 引入BC库
		Security.addProvider(new BouncyCastleProvider());
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509","BC");
		X509Certificate cacert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(certPath));

		AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SM3WITHSM2");
		AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

		org.bouncycastle.asn1.x500.X500Name issuer = new org.bouncycastle.asn1.x500.X500Name(cacert.getSubjectX500Principal().getName());
		BigInteger serial = new BigInteger(32, new SecureRandom());
		Date from = new Date();
		Date to = new Date(System.currentTimeMillis() + (365 * 2 * 86400000L));

		// 构建证书
		X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(issuer, serial, from, to, csr.getSubject(),
				csr.getSubjectPublicKeyInfo());

		//ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));

		ContentSigner signer = new BcECContentSignerBuilder(sigAlgId, digAlgId).build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));
		// 使用CA私钥签署证书
		X509CertificateHolder certificateHolder = certificateBuilder.build(signer);
		X509Certificate caCertificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateHolder);
		return caCertificate;
	}
}
