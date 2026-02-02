package com.fib.midbiz.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fib.midbiz.app.MidbizApplication;
import com.fib.midbiz.common.dto.EfsDTO;
import com.fib.midbiz.efs.api.EfsServiceApi;

@SpringBootTest(classes = MidbizApplication.class)
public class EfsServiceApiTest {
	@Autowired
	EfsServiceApi efsServiceApi;

	@Test
	public void test() {
		EfsDTO efsDTO = efsServiceApi.createEfs();
		assertNotNull(efsDTO);
	}
}
