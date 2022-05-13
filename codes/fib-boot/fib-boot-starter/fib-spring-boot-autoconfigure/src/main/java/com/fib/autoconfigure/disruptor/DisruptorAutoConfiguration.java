package com.fib.autoconfigure.disruptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.lmax.disruptor.dsl.Disruptor;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Disruptor.class)
@ComponentScan("com.fib.autoconfigure.disruptor")
public class DisruptorAutoConfiguration {

}
