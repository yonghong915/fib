package com.fib.autoconfigure.aop;

import org.aspectj.lang.JoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.fib.autoconfigure.aop")
@ConditionalOnClass({ JoinPoint.class })
public class EcaAutoConfiguration {

}
