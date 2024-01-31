package com.fib.autoconfigure.crypto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.fib.autoconfigure.crypto.advice.DecryptRequestBodyAdvice;
import com.fib.autoconfigure.crypto.advice.EncryptResponseBodyAdvice;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({ EncryptResponseBodyAdvice.class, DecryptRequestBodyAdvice.class })
public @interface EnableSecurity {

}