package com.fib.ecny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@ComponentScan(basePackages = "com.fib.ecny")
@EnableAsync
@ConfigurationPropertiesScan
public class EcnyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcnyApplication.class, args);
    }
}
