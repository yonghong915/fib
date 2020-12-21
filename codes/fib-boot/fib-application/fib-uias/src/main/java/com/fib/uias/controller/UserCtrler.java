package com.fib.uias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fib.uias.entity.UserEntity;
import com.fib.uias.service.IUserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@RestController
@RequestMapping("/user")
public class UserCtrler {
	@Autowired
	private IUserService userService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Value("${serviceName:fib-uias}")
	private String serviceName;

	@GetMapping(value = "/saveUser")
	public UserEntity saveUser() {
		UserEntity userEntity = new UserEntity();
		Snowflake sf = IdUtil.getSnowflake(1, 1);
		long pkId = sf.nextId();
		userEntity.setPkId(pkId);
		String userCode = "1234456";
		userEntity.setUserCode(userCode);
		userEntity.setRealName("真实姓名");
		userEntity.setUserDesc("desc");
		userEntity.setUserType(1);
		userService.addUser(userEntity);
		return userEntity;
	}

	@GetMapping(value = "/transfer")
	public String transferAccounts() {
		try {
			userService.retryTransferAccounts(1, 2, 200);
			return "ok";
		} catch (Exception e) {
			return "no";
		}
	}

	@PostMapping(value = "/getUser")
	public UserEntity get(String userCode) {
		System.out.println("userCode=" + userCode);

		try {
			Thread.sleep(60 * 60 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userService.getUser(userCode);
	}

	@GetMapping("/add/{name}")
	@HystrixCommand(fallbackMethod = "SaveFallbackMethod", commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
			@HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "3") })
	public String add(@PathVariable String name) {
		String url = "http://" + serviceName + "/add/" + name;
		ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);
		return respEntity.getBody();
	}

	public String SaveFallbackMethod(@PathVariable String name) {
		String data = "稍后再试";
		return data;
	}
}
