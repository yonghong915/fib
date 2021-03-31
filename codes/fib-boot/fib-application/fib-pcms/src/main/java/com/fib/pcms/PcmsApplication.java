package com.fib.pcms;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-03-31
 */
@SpringBootApplication
public class PcmsApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PcmsApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
