package com.fib.upp.ctrler;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.upp.entity.UserEntity;
import com.fib.upp.service.IUserService;

@RestController
@RequestMapping("/app")
public class AppCtrler {
	@Resource
	private IUserService userService;

	@GetMapping("/getStr")
	public String getStr() {
		List<UserEntity> userList = userService.list();
		return "hello";
	}

	/**
	 * 新增
	 *
	 * @return
	 */
	@GetMapping("/getSaveUser")
	public Object getSaveUser() {
		UserEntity user = new UserEntity();
		user.setId(UUID.randomUUID().toString().substring(0, 10));
		user.setName("李某某");
		user.setAge(21);
		user.setSex("女");
		boolean save = userService.save(user);
		if (save) {
			return "新增成功！！！";
		}
		return null;
	}
}
