package com.fib.upp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.minio.service.IMinioService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class MinioTest {
	@Autowired
	private IMinioService minioService;

	@Test
	public void testGetMessagePackRule() {
		String bucketName = "upp";
		boolean existsFlg = minioService.bucketExists(bucketName);
		Assert.assertTrue(existsFlg);
	}
}