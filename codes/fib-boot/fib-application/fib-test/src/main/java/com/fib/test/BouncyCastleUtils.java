//package com.fib.test;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.math.BigInteger;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.KeyStore;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.SecureRandom;
//import java.security.Security;
//import java.security.cert.Certificate;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import javax.security.auth.x500.X500Principal;
//
//import org.apache.tomcat.util.http.fileupload.FileUtils;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.bouncycastle.asn1.ASN1InputStream;
//import org.bouncycastle.asn1.DEROctetString;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.asn1.x500.X500NameBuilder;
//import org.bouncycastle.asn1.x500.style.BCStyle;
//import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
//import org.bouncycastle.asn1.x509.BasicConstraints;
//import org.bouncycastle.asn1.x509.Extension;
//import org.bouncycastle.asn1.x509.KeyUsage;
//import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
//import org.bouncycastle.cert.X509CertificateHolder;
//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
//import org.bouncycastle.crypto.util.PrivateKeyFactory;
//import org.bouncycastle.openssl.PEMParser;
//import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
//import org.bouncycastle.operator.ContentSigner;
//import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
//import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
//import org.bouncycastle.operator.OperatorCreationException;
//import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
//import org.bouncycastle.pkcs.PKCS10CertificationRequest;
//import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
//import org.bouncycastle.util.io.pem.PemWriter;
//
//import cn.hutool.core.date.DateUtil;
//
//public class BouncyCastleUtils {
//	
//	/**
//	 * 1.生成密匙对,存储KeyStore 2.生成CSR证书请求文件 3.生成CER证书
//	 */
//
//	/**
//	 * 默认生成密钥算法
//	 */
//	private static final String DEFAULT_KEY_ALGORITHM = "RSA";//SM2
//
//	/**
//	 * 签名算法
//	 */
//	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";//SM3withSM2
//
//	/**
//	 * 默认生成密钥长度
//	 */
//	private static final int DEFAULT_KEY_SIZE = 2048;
//
//	/**
//	 * JAVA KEY STORE JKS
//	 */
//	private static final String KEY_STORE_JKS = "JKS";
//
//	/**
//	 * JAVA KEY STORE PWD
//	 */
//	private static final String KEY_STORE_PWD = "123456";
//
//	/**
//	 * JAVA 证书格式
//	 */
//	private static final String CERTFICATE_TYPE = "X.509";
//
//	/**
//	 * 文件根路径
//	 */
//	private static final String BASE_FILE_PATH = "/YlDream/Idea2022/ideaWorkMe/opFile/";
//
//	/** 设置提供商 */
//	static {
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//	}
//
//	/** =====================生成KeyPair,存储KeyStore================= */
//
//	/**
//	 * RSA生成密匙对
//	 *
//	 * @return
//	 * @throws NoSuchAlgorithmException 
//	 */
//	//@SneakyThrows
//	public static KeyPair createKeyPair() throws NoSuchAlgorithmException {
//		KeyPairGenerator generator = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGORITHM);
//		generator.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
//		KeyPair keyPair = generator.generateKeyPair();
//		return keyPair;
//	}
//
//	/**
//	 * 随机生成别名
//	 *
//	 * @return
//	 */
//	public static String getKeyPairAlias() {
//		return "__yl_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "-" + DateUtil.now();
//	}
//
//	/**
//	 * 随机生成文件名
//	 *
//	 * @return
//	 */
//	public static String getTempFileName() {
//		return DateUtil.format(new Date(), "yyyyMMddHHmmss") + "-" + DateUtil.current();
//	}
//
//	/**
//	 * 构建X500格式
//	 *
//	 * @param country            国别
//	 * @param stateOrProvince    州或省的名称
//	 * @param locality           城市或区域
//	 * @param organization       组织
//	 * @param organizationalUnit 组织单位名称
//	 * @param commonName         常用名
//	 * @param emailAddress       Verisign证书中的电子邮件地址
//	 * @return
//	 */
//	public static X500Name getX500Name(String country, String stateOrProvince, String locality, String organization, String organizationalUnit,
//			String commonName, String emailAddress) {
//		X500NameBuilder builder = new X500NameBuilder(X500Name.getDefaultStyle());
//		builder.addRDN(BCStyle.C, country);
//		builder.addRDN(BCStyle.ST, stateOrProvince);
//		builder.addRDN(BCStyle.L, locality);
//		builder.addRDN(BCStyle.O, organization);
//		builder.addRDN(BCStyle.OU, organizationalUnit);
//		builder.addRDN(BCStyle.CN, commonName);
//		builder.addRDN(BCStyle.E, emailAddress);
//		return builder.build();
//	}
//
//	/**
//	 * 获取签名内容？
//	 *
//	 * @param privateKey         私钥
//	 * @param signatureAlgorithm 签名算法
//	 * @return
//	 * @throws IOException
//	 * @throws OperatorCreationException
//	 */
//	private static ContentSigner getContentSigner(PrivateKey privateKey, String signatureAlgorithm) throws IOException, OperatorCreationException {
//		AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(signatureAlgorithm);
//		AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
//		AsymmetricKeyParameter keyParam = PrivateKeyFactory.createKey(privateKey.getEncoded());
//		ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(keyParam);
//		return signer;
//	}
//
//	/**
//	 * 生成签发证书的基础方法
//	 *
//	 * @param signatureAlgorithm 签名算法 SHA256WithRSA
//	 * @param privateKey         签发者私钥
//	 * @param publicKeyInfo      被签发者公钥信息
//	 * @param subjectDn          被签发者subject
//	 * @param issuerDn           签发者subject
//	 * @param notBefore          开始时间
//	 * @param notAfter           结束时间
//	 * @param extensions         扩展信息
//	 * @return
//	 * @throws Exception
//	 */
//	public static X509Certificate generateCertifCate(String signatureAlgorithm, PrivateKey privateKey, SubjectPublicKeyInfo publicKeyInfo,
//			X500Name subjectDn, X500Name issuerDn, Date notBefore, Date notAfter, List<Extension> extensions) throws Exception {
//		BigInteger serial = BigInteger.probablePrime(32, new Random());
//		X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuerDn, serial, notBefore, notAfter, subjectDn, publicKeyInfo);
//
//		if (extensions != null && extensions.size() > 0) {
//			for (Extension extension : extensions) {
//				if (extension == null) {
//					continue;
//				}
//				builder.addExtension(extension.getExtnId(), extension.isCritical(), extension.getParsedValue());
//			}
//		}
//		ContentSigner signer = getContentSigner(privateKey, signatureAlgorithm);
//		X509CertificateHolder holder = builder.build(signer);
//		// BC加密算法
//		X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
//		cert.verify(cert.getPublicKey());
//		return cert;
//	}
//
//	/**
//	 * 公钥 - 包含存储在证书中的公钥的对象。
//	 *
//	 * @param publicKey 公钥
//	 * @return
//	 * @throws Exception
//	 */
//	public static SubjectPublicKeyInfo convert(PublicKey publicKey) throws Exception {
//		// JCE中公钥中的getEncoded()方法生成一个对其中之一进行编码的DER。
//		ASN1InputStream asn1InputStream = new ASN1InputStream(publicKey.getEncoded());
//		try {
//			SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(asn1InputStream.readObject());
//			return publicKeyInfo;
//		} finally {
//			asn1InputStream.close();
//		}
//	}
//
//	/**
//	 * 获取存储到磁盘的文件对象
//	 *
//	 * @param dbStoreName
//	 * @return
//	 */
//	public static File getStoreFile(String dbStoreName) {
//		File file = new File(BASE_FILE_PATH, dbStoreName);
//		if (file.getParentFile().exists() == false) {
//			file.getParentFile().mkdirs();
//		}
//		return file;
//	}
//
//	/**
//	 * 存储keyPair到KeyStore
//	 *
//	 * @param keyPair
//	 * @throws Exception 
//	 */
//	//@SneakyThrows
//	public static Map<String, String> saveKeyStore(KeyPair keyPair) throws Exception {
//		Map<String, String> result = new HashMap<>();
//		// 获取KeyStore
//		KeyStore keyStore = KeyStore.getInstance(KEY_STORE_JKS);
//		keyStore.load(null, KEY_STORE_PWD.toCharArray());
//		// 密钥对以证书的形式存储
//		// 别名
//		String alias = getKeyPairAlias();
//		// 生成X500Name
//		X500Name subjectDn = getX500Name("CN", "Shanghai", "Shanghai", "dream", "dev", "dream-keyPair", "contactus@wdreaam.com");
//		// 公钥
//		PrivateKey privateKey = keyPair.getPrivate();
//		// 私钥
//		PublicKey publicKey = keyPair.getPublic();
//		// 构建证书
//		X509Certificate cert = generateCertifCate(SIGNATURE_ALGORITHM, privateKey, convert(publicKey), subjectDn, subjectDn, new Date(), new Date(),
//				null);
//		// 设置到KeyEntry
//		keyStore.setKeyEntry(alias, keyPair.getPrivate(), KEY_STORE_PWD.toCharArray(), new Certificate[] { cert });
//		// keyStore存储文件地址
//		String keyStorePath = "/keystore/" + getTempFileName() + ".jks";
//		File keyStoreFile = getStoreFile(keyStorePath);
//		System.out.println("keyStore存储文件地址===>" + keyStoreFile.getAbsolutePath());
//		// 保存
//		try (FileOutputStream out = new FileOutputStream(keyStoreFile)) {
//			keyStore.store(out, KEY_STORE_PWD.toCharArray());
//			try {
//				out.close();
//			} catch (Exception e) {
//			}
//		}
//		result.put("alias", alias);
//		result.put("keyStorePath", keyStorePath);
//		System.out.println("存储keyPair到KeyStore Success");
//		return result;
//	}
//
//	/**
//	 * 从keyStore获取密匙对
//	 *
//	 * @param alias        密钥别名
//	 * @param keyStoreFile 密钥存储文件File
//	 * @return
//	 * @throws Exception
//	 */
//	public static KeyPair getKeyPair(String alias, File keyStoreFile) throws Exception {
//		// 获取KeyStore
//		KeyStore keyStore = KeyStore.getInstance(KEY_STORE_JKS);
//		keyStore.load(new FileInputStream(keyStoreFile), KEY_STORE_PWD.toCharArray());
//		// 获取私钥
//		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, KEY_STORE_PWD.toCharArray());
//		// 获取公钥
//		X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
//		PublicKey publicKey = x509Certificate.getPublicKey();
//		return new KeyPair(publicKey, privateKey);
//	}
//
//	/** =====================生成CSR请求文件,存储到本地================= */
//	/**
//	 * 生成csr
//	 *
//	 * @param privateKey    私钥
//	 * @param publicKeyInfo 公钥
//	 * @param subject       主题
//	 * @param extensions    公钥信息
//	 * @return
//	 * @throws Exception
//	 */
//	public static PKCS10CertificationRequest generateCsr(String signatureAlgorithm, PrivateKey privateKey, SubjectPublicKeyInfo publicKeyInfo,
//			X500Name subject, List<Extension> extensions) throws Exception {
//		PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(subject, publicKeyInfo);
//
//		if (extensions != null && extensions.size() > 0) {
//			for (Extension extension : extensions) {
//				if (extension == null) {
//					continue;
//				}
//				builder.addAttribute(extension.getExtnId(), extension.getParsedValue());
//			}
//		}
//		ContentSigner signer = getContentSigner(privateKey, signatureAlgorithm);
//		PKCS10CertificationRequest csr = builder.build(signer);
//		return csr;
//	}
//
//	/**
//	 * 组装DN
//	 *
//	 * @param countryName            国家名称 C
//	 * @param stateOrProvinceName    省市名称 ST
//	 * @param localityName           城市名称 L
//	 * @param organizationName       组织名称 O
//	 * @param organizationalUnitName 组织单元名称 OU
//	 * @param commonName             常用名称 CN
//	 * @param emailAddress           邮件地址 emailAddress
//	 * @return
//	 */
//	public static String generateDN(String countryName, String stateOrProvinceName, String localityName, String organizationName,
//			String organizationalUnitName, String commonName, String emailAddress) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("EMAILADDRESS=").append(emailAddress).append(",");
//		sb.append("CN=").append(commonName).append(",");
//		sb.append("OU=").append(organizationalUnitName).append(",");
//		sb.append("O=").append(organizationName).append(",");
//		sb.append("L=").append(localityName).append(",");
//		sb.append("ST=").append(stateOrProvinceName).append(",");
//		sb.append("C=").append(countryName);
//		return sb.toString();
//	}
//
//	/**
//	 * 扩展
//	 **/
//	private static final Map<String, Integer> KEY_USAGE = new HashMap<>();
//
//	/**
//	 * 构建扩展
//	 *
//	 * @param basicConstraintsType
//	 * @param extensionsTypeCritical
//	 * @param keyUsage
//	 * @param keyUsageCritical
//	 * @return
//	 * @throws Exception
//	 */
//	public static List<Extension> getExtensions(String basicConstraintsType, boolean extensionsTypeCritical, String[] keyUsage,
//			boolean keyUsageCritical) throws Exception {
//		List<Extension> extensions = new ArrayList<Extension>();
//		if (StringUtils.isNotEmpty(basicConstraintsType) && !basicConstraintsType.equals("NotDefined")) {
//			BasicConstraints basicConstraints = new BasicConstraints(basicConstraintsType.equals("CertificationAuthority"));
//			Extension extension = new Extension(Extension.basicConstraints, extensionsTypeCritical, new DEROctetString(basicConstraints));
//			extensions.add(extension);
//		}
//		if (keyUsage != null && keyUsage.length > 0) {
//			int result = 0;
//			if (keyUsage != null && keyUsage.length > 0) {
//				for (String usage : keyUsage) {
//					if (usage == null || usage.length() < 1) {
//						continue;
//					}
//					result |= KEY_USAGE.get(usage.toLowerCase());
//				}
//			}
//			KeyUsage keyUsage2 = new KeyUsage(result);
//			Extension extension = new Extension(Extension.keyUsage, keyUsageCritical, new DEROctetString(keyUsage2));
//			extensions.add(extension);
//		}
//		return extensions;
//	}
//
//	/**
//	 * 创建CSR证书请求文件
//	 *
//	 * @param alias
//	 * @param keyStorePath
//	 * @return
//	 * @throws Exception 
//	 */
//	//@SneakyThrows
//	public static Map<String, Object> createCsr(String alias, String keyStorePath) throws Exception {
//		Map<String, Object> result = new HashMap<>();
//		// 主题
//		String subjectDn = generateDN("CN", "Shanghai", "Shanghai", "dream", "dev", "dream-keyPair", "contactus@wdreaam.com");
//		// 公钥用途
//		String keyUsage = "";
//		String[] keyUsages = null;
//		if (keyUsage != null) {
//			keyUsages = keyUsage.split(",");
//		}
//		X500Name subject = new X500Name(subjectDn);
//		KeyPair keyPair = getKeyPair(alias, getStoreFile(keyStorePath));
//		// 公钥用途扩展
//		List<Extension> extensions = getExtensions(null, false, keyUsages, true);
//		// CSR证书请求文件
//		PKCS10CertificationRequest certificationRequest = generateCsr(SIGNATURE_ALGORITHM, keyPair.getPrivate(), convert(keyPair.getPublic()),
//				subject, extensions);
//		// CSR文件写入到本地
//		String csrPath = "/csr/" + getTempFileName() + ".csr";
//		File csrFile = getStoreFile(csrPath);
//		System.out.println("CSR存储文件地址===>" + csrFile.getAbsolutePath());
//		byte[] bytes = certificationRequest.getEncoded();
//		String s = Base64.getEncoder().encodeToString(bytes);
//		System.out.println("-----BEGIN CSR-----");
//		System.out.println(s);
//		System.out.println("-----END CSR-----");
//		OutputStream out = new FileOutputStream(csrFile);
//		PemWriter writer = new PemWriter(new OutputStreamWriter(out));
//		writer.writeObject(new JcaMiscPEMGenerator(certificationRequest));
//		writer.close();
//		result.put("csr", certificationRequest);
//		result.put("csrPath", csrPath);
//		return result;
//	}
//
//	/**
//	 * ======================生成/签发Certificate证书,存储到本地=================
//	 */
//
//	/**
//	 * 判断文件格式pem(true)和der(false)
//	 *
//	 * @param file
//	 * @return
//	 */
//	public static boolean isPemFormat(File file) {
//		try {
//			FileInputStream inStream = new FileInputStream(file);
//			byte[] b = new byte[4];
//			inStream.read(b);
//			inStream.close();
//			if (Arrays.equals(b, new byte[] { 0x2D, 0x2D, 0x2D, 0x2D })) {
//				return true;
//			}
//			return false;
//		} catch (IOException e) {
//			//log.error("", e);
//		}
//		return false;
//	}
//
//	/**
//	 * 生成Certificate证书存储到本地
//	 *
//	 * @param alias        密钥别名
//	 * @param keyStorePath 密钥文件
//	 * @param parentCert   父级证书
//	 * @return
//	 * @throws Exception 
//	 */
//	//@SneakyThrows
//	public static Map<String, Object> createCert(String alias, String keyStorePath, X509Certificate parentCert) throws Exception {
//		Map<String, Object> result = new HashMap<>();
//		// X509格式证书
//		X509Certificate cert = null;
//		// 对应参数
//		Date notBefore = DateUtil.date();
//		Date notAfter = DateUtil.date();
//		// 主题 || 父节点主题DN
//		String subjectDn = generateDN("selfSubject-CN", "Shanghai", "Shanghai", "dream", "dev", "dream-keyPair", "contactus@wdreaam.com");
//		// 子级节点
//		X500Name selfSubject = new X500Name(subjectDn);
//		String subjectParentDn = generateDN("subjectParent-CN", "Shanghai", "Shanghai", "dream", "dev", "dream-keyPair", "contactus@wdreaam.com");
//		// 父级节点
//		X500Name issuerSubject = new X500Name(subjectParentDn);
//		// 密匙对 || 获取父级节点证书的密钥对
//		KeyPair keyPair = getKeyPair(alias, getStoreFile(keyStorePath));
//		// 公钥用途扩展
//		String keyUsage = "";
//		String[] keyUsages = null;
//		if (keyUsage != null) {
//			keyUsages = keyUsage.split(",");
//		}
//		List<Extension> extensions = getExtensions(null, false, keyUsages, true);
//		// 文件存储地址
//		String certPath = "/cert/" + getTempFileName() + ".cert";
//		if (parentCert == null) {
//			/**
//			 * 1.自签名证书
//			 */
//			cert = generateCertifCate(SIGNATURE_ALGORITHM, keyPair.getPrivate(), convert(keyPair.getPublic()), selfSubject, selfSubject, notBefore,
//					notAfter, extensions);
//		} else {
//			/**
//			 * 2.利用父证书签发当前正在创建的证书 密钥使用父级节点密钥 主题使用父级节点主题
//			 */
//			System.out.println("CertifCate父级节点主题===>" + parentCert.getSubjectDN().getName());
//			certPath = "/cert/child-" + getTempFileName() + ".cert";
//			cert = generateCertifCate(SIGNATURE_ALGORITHM, keyPair.getPrivate(), convert(keyPair.getPublic()), selfSubject, issuerSubject, notBefore,
//					notAfter, extensions);
//
//		}
//		// 证书以文件形式保存到磁盘
//		File certFile = getStoreFile(certPath);
//		System.out.println("CertifCate存储文件地址===>" + certFile.getAbsolutePath());
//		FileOutputStream out = new FileOutputStream(certFile);
//		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
//		PemWriter writer = new PemWriter(outputStreamWriter);
//		writer.writeObject(new JcaMiscPEMGenerator(cert));
//		writer.close();
//		result.put("cert", cert);
//		result.put("certPath", certPath);
//		return result;
//	}
//
//	/**
//	 * 签发Certificate证书存储到本地(签发证书签名请求)
//	 *
//	 * @param alias          密钥别名
//	 * @param keyStorePath   密钥文件
//	 * @param certIssuerPath Certificate证书文件
//	 * @param csrIssuerFile  CSR证书请求文件
//	 * @return
//	 */
//	//@SneakyThrows
//	public static X509Certificate issuerCsr(String alias, String keyStorePath, String certIssuerPath, File csrIssuerFile) {
//		// X509格式证书
//		X509Certificate cert = null;
//		/********************************* 读取CSR文件 *******************************/
//		// 判断文件格式pem(true)和der(false)
//		System.out.println("签发证书CSR请求文件地址===>" + csrIssuerFile.getAbsolutePath());
//		PKCS10CertificationRequest csr = null;
//		FileInputStream inStream = new FileInputStream(csrIssuerFile);
//		if (isPemFormat(csrIssuerFile)) {
//			PEMParser parser = new PEMParser(new InputStreamReader(inStream));
//			csr = (PKCS10CertificationRequest) parser.readObject();
//		} else {
//			csr = new PKCS10CertificationRequest(IOUtils.toByteArray(inStream));
//		}
//		/*********************************
//		 * 读取Certificate文件
//		 *******************************/
//		System.out.println("签发证书Certificate文件地址===>" + certIssuerPath);
//		File certIssuerFile = getStoreFile(certIssuerPath);
//		// 判断文件格式pem(true)和der(false)
//		X509Certificate certIssuer = null;
//		if (isPemFormat(certIssuerFile)) {
//			PEMParser parser = new PEMParser(new FileReader(certIssuerFile));
//			try {
//				Object obj = parser.readObject();
//				if (obj instanceof X509CertificateHolder) {
//					X509CertificateHolder holder = (X509CertificateHolder) obj;
//					byte[] certBuf = holder.getEncoded();
//					CertificateFactory certFactory = CertificateFactory.getInstance(CERTFICATE_TYPE, "BC");
//					certIssuer = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certBuf));
//				} else {
//					certIssuer = (X509Certificate) obj;
//				}
//			} finally {
//				parser.close();
//			}
//		} else {
//			inStream = new FileInputStream(certIssuerFile);
//			CertificateFactory certFactory = CertificateFactory.getInstance(CERTFICATE_TYPE);
//			certIssuer = (X509Certificate) certFactory.generateCertificate(inStream);
//		}
//		// 获取父级主题
//		X500Principal principal = certIssuer.getSubjectX500Principal();
//		X500Name parentSubjectDn = X500Name.getInstance(principal.getEncoded());
//		/********************************* 签发证书 *******************************/
//		// 对应参数
//		Date notBefore = DateUtil.date();
//		Date notAfter = DateUtil.date();
//		// 密匙对
//		KeyPair keyPair = getKeyPair(alias, getStoreFile(keyStorePath));
//		// 公钥用途扩展
//		String keyUsage = "";
//		String[] keyUsages = null;
//		if (keyUsage != null) {
//			keyUsages = keyUsage.split(",");
//		}
//		List<Extension> extensions = getExtensions(null, false, keyUsages, true);
//		// 生成签发的证书文件
//		cert = generateCertifCate(SIGNATURE_ALGORITHM, keyPair.getPrivate(), csr.getSubjectPublicKeyInfo(), csr.getSubject(), parentSubjectDn,
//				notBefore, notAfter, extensions);
//		// 证书以文件形式保存到磁盘
//		String certPath = "/cert/issuer-" + getTempFileName() + ".cert";
//		File certFile = getStoreFile(certPath);
//		System.out.println("签发证书生成CertifCate存储文件地址===>" + certFile.getAbsolutePath());
//		FileOutputStream out = new FileOutputStream(certFile);
//		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
//		PemWriter writer = new PemWriter(outputStreamWriter);
//		writer.writeObject(new JcaMiscPEMGenerator(cert));
//		writer.close();
//
//		String csrPath = "/csr/issuer-" + getTempFileName() + ".csr";
//		File csrStoreFile = getStoreFile(csrPath);
//		//FileUtils.copyFile(csrIssuerFile, csrStoreFile);
//		System.out.println("签发证书生成CSR存储文件地址===>" + csrStoreFile.getAbsolutePath());
//		return cert;
//	}
//
//	//@SneakyThrows
//	public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
//		System.out.println("=====================生成KeyPair,存储KeyStore=================");
//		Map<String, String> result = saveKeyStore(createKeyPair());
//		KeyPair keyPair = getKeyPair(result.get("alias"), getStoreFile(result.get("keyStorePath")));
//		System.out.println("private Key===>" + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
//		System.out.println("public Key===>" + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
//		System.out.println("=====================生成CSR请求文件,存储到本地=================");
//		Map<String, Object> csrResult = createCsr(result.get("alias"), result.get("keyStorePath"));
//		String csrPath = (String) csrResult.get("csrPath");
//		System.out.println("=====================生成Certificate证书,存储到本地=================");
//		Map<String, Object> parentResult = createCert(result.get("alias"), result.get("keyStorePath"), null);
//		X509Certificate parentCert = (X509Certificate) parentResult.get("cert");
//		String certPath = (String) parentResult.get("certPath");
//		createCert(result.get("alias"), result.get("keyStorePath"), parentCert);
//		System.out.println("=====================签发Certificate证书,存储到本地=================");
//		issuerCsr(result.get("alias"), result.get("keyStorePath"), certPath, getStoreFile(csrPath));
//	}
//
//}
