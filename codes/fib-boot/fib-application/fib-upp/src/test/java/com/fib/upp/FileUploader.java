package com.fib.upp;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.minio.service.IMinioService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class FileUploader {
	@Autowired
	private IMinioService minioService;

	@Test
	public void testGetMessagePackRule() {
		String bucketName = "upp";
		boolean existsFlg = minioService.bucketExists(bucketName);
		Assert.assertTrue(existsFlg);
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

//		try {
//			// Create a minioClient with the MinIO server playground, its access key and
//			// secret key.
//			MinioClient minioClient = MinioClient.builder().endpoint("http://192.168.56.11:9000/").credentials("fangyh", "fangyh102034").build();
//            String bucketName = "upp";
//			// Make 'asiatrip' bucket if not exist.
//			boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//			if (!found) {
//				// Make a new bucket called 'asiatrip'.
//				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//			} else {
//				System.out.println("Bucket 'asiatrip' already exists.");
//			}
//
//			// Upload '/home/user/Photos/asiaphotos.zip' as object name
//			// 'asiaphotos-2015.zip' to bucket
//			// 'asiatrip'.
//		
//			minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object("20220411/party.sql").filename("F:/party.sql").build());
//			System.out.println(
//					"'/home/user/Photos/asiaphotos.zip' is successfully uploaded as " + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
//		} catch (MinioException e) {
//			System.out.println("Error occurred: " + e);
//			System.out.println("HTTP trace: " + e.httpTrace());
//		}

	}
}
