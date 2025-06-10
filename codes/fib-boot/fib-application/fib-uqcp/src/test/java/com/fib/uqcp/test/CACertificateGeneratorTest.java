package com.fib.uqcp.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.Test;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;

public class CACertificateGeneratorTest {

	@Test
	public void testGenKey() {
		com.fib.autoconfigure.crypto.security.util.CACertificateGenerator generator = new com.fib.autoconfigure.crypto.security.util.CACertificateGenerator();
		try {
			com.fib.autoconfigure.crypto.security.util.CACertificateGenerator.genCACertificate("SM2", "SM3WITHSM2", "d:/private_key.pem",
					"d:/public_key.pem");

			byte[] privateKey = null;
			try (PemReader pemReader = new PemReader(new FileReader("D://private_key.pem"))) {
				PemObject pemObject = pemReader.readPemObject();

				// 获取pem对象中的私钥字节数组
				privateKey = pemObject.getContent();
			}

			try {
//				Security.addProvider(new BouncyCastleProvider());
//				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
//				KeyFactory keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
//				PublicKey pubKey = keyFactory.generatePublic(keySpec);
//
//				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
//				keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
//				// 取私钥匙对象
//				PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

				KeyPair keyPair = SecureUtil.generateKeyPair("SM2", 2048);

				PublicKey pubKey = keyPair.getPublic();
				PrivateKey priKey = keyPair.getPrivate();

				try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("D:\\sub_private_key.pem"));) {
					pemWriter.writeObject(priKey);
				}

				PKCS10CertificationRequest csr = com.fib.autoconfigure.crypto.security.util.CACertificateGenerator.generateCSR(priKey, pubKey);

				X509Certificate certificate = com.fib.autoconfigure.crypto.security.util.CACertificateGenerator.generateCACertificate(csr,
						"D://public_key.pem", priKey);

				try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("D:\\sub_public_key.pem"));) {
					pemWriter.writeObject(certificate);
				}

				try (PemReader pemReader = new PemReader(new FileReader("D://sub_private_key.pem"))) {
					PemObject pemObject = pemReader.readPemObject();

					// 获取pem对象中的私钥字节数组
					privateKey = pemObject.getContent();
				}
				System.out.println(Base64.encode(privateKey));
				System.out.println(Base64.encode(priKey.getEncoded()));

//				X509Certificate cer = (X509Certificate) SecureUtil.readX509Certificate(FileUtil.getInputStream(new File("D:\\sub_public_key.pem")));
//				byte[] publicKey = cer.getEncoded();
				try (FileReader keyReader = new FileReader(new File("D:\\sub_public_key.pem"))) {
			        PEMParser pemParser = new PEMParser(keyReader);
			        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
			    	System.out.println(Base64.encode( publicKeyInfo.parsePublicKey().getEncoded()));
			    }
				
			
				System.out.println(Base64.encode(pubKey.getEncoded()));
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (OperatorCreationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
