package com.fib.ecny.trans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fib.ecny")
public class EcnyTransApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcnyTransApplication.class, args);
    }
}
