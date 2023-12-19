package com.fib.commons.security.support.sha;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SHA256 extends Digester {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SHA256(DigestAlgorithm algorithm) {
		super(algorithm);
	}

	/**
	 * 创建SM3实例
	 *
	 * @return SM3
	 * @since 4.6.0
	 */
	public static SHA256 create() {
		return new SHA256();
	}

	/**
	 * 构造
	 */
	public SHA256() {
		super(DigestAlgorithm.SHA256);
	}

	/**
	 * 构造
	 *
	 * @param salt 盐值
	 */
	public SHA256(byte[] salt) {
		this(salt, 0, 1);
	}

	/**
	 * 构造
	 *
	 * @param salt        盐值
	 * @param digestCount 摘要次数，当此值小于等于1,默认为1。
	 */
	public SHA256(byte[] salt, int digestCount) {
		this(salt, 0, digestCount);
	}

	/**
	 * 构造
	 *
	 * @param salt         盐值
	 * @param saltPosition 加盐位置，即将盐值字符串放置在数据的index数，默认0
	 * @param digestCount  摘要次数，当此值小于等于1,默认为1。
	 */
	public SHA256(byte[] salt, int saltPosition, int digestCount) {
		this();
		this.salt = salt;
		this.saltPosition = saltPosition;
		this.digestCount = digestCount;
	}
}
