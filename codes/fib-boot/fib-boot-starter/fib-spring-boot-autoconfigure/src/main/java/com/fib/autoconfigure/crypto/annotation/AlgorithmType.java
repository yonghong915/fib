package com.fib.autoconfigure.crypto.annotation;

public interface AlgorithmType {
	interface Algorithm {
		String getValue();
	}

	/**
	 * 对称算法枚举
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2024-01-31 15:19:38
	 */
	public enum SymmetricAlgorithm implements Algorithm {
		/**
		 * AES算法
		 */
		AES("AES"),
		/**
		 * SM4算法
		 */
		SM4("SM4");

		private final String value;

		SymmetricAlgorithm(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return this.value;
		}
	}

	/**
	 * 非对称算法枚举
	 * 
	 * @author fangyh
	 * @version 1.0
	 * @date 2024-01-31 15:19:50
	 */
	public enum AsymmetricAlgorithm implements Algorithm {
		/** RSA算法 */
		RSA("RSA"),
		/** SM2算法 */
		SM2("SM2");

		private final String value;

		AsymmetricAlgorithm(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return this.value;
		}
	}

	public enum DigestAlgorithm implements Algorithm {
		MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512"), SM3("SM3");

		private final String value;

		DigestAlgorithm(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return this.value;
		}
	}
}