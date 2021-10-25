package com.fib.core.annotation.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.fib.core.advice.security.EncryptRequestBodyAdvice;
import com.fib.core.advice.security.EncryptResponseBodyAdvice;
import com.fib.core.config.SecretKeyConfig;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({ SecretKeyConfig.class, EncryptResponseBodyAdvice.class, EncryptRequestBodyAdvice.class })
public @interface EnableSecurity {

}