package com.fib.autoconfigure.crypto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密注解
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-31 14:47:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

/**
 * 对称加密算法
 * 
 * @return
 */
AlgorithmType.SymmetricAlgorithm symmetricAlgorithm() default AlgorithmType.SymmetricAlgorithm.AES;

/**
 * 非对称加密算法
 * 
 * @return
 */
AlgorithmType.AsymmetricAlgorithm asymmetricAlgorithm() default AlgorithmType.AsymmetricAlgorithm.RSA;

/**
 * 摘要算法类型
 */
AlgorithmType.DigestAlgorithm digestAlgorithm() default AlgorithmType.DigestAlgorithm.SHA256;
}