package com.fib.uias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.uias.entity.UserEntity;
import com.fib.uias.service.IUserService;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@RestController
@RequestMapping("/user")
public class UserCtrler {
	@Autowired
	private IUserService userService;

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
}
