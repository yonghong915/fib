package com.fib.ecny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fib.ecny")
public class EcnyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcnyApplication.class, args);
    }
}
