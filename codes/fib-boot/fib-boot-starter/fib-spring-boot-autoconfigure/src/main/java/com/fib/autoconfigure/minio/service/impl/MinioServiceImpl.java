package com.fib.autoconfigure.minio.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fib.autoconfigure.minio.service.IMinioService;
import com.fib.commons.exception.CommonException;

import io.minio.BucketExistsArgs;
import io.minio.GetBucketPolicyArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.BucketPolicyTooLargeException;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

/**
 * Minio服务
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-11 09:43:09
 */
@Service("minioService")
public class MinioServiceImpl implements IMinioService {

	private MinioClient minioClient;

	public MinioServiceImpl(MinioClient minioClient) {
		this.minioClient = minioClient;
	}

	@Override
	public boolean bucketExists(String bucketName) {
		try {
			return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public void createBucket(String bucketName) {
		try {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public void putObject(String bucketName, String objectName, InputStream is) {
		try {
			minioClient.putObject(
					PutObjectArgs.builder().bucket(bucketName).contentType(objectName).object(objectName).stream(is, is.available(), -1).build());
			is.close();
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public void uploadObject(String bucketName, String objectName, String fileName) {
		try {
			minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	public InputStream getObject(String bucketName, String objectName) {
		try {
			return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public InputStream getObject(String bucketName, String objectName, long offset, long length) {
		try {
			return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(length).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public String getBucketPolicy(String bucketName) {
		try {
			return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
		} catch (InvalidKeyException | BucketPolicyTooLargeException | ErrorResponseException | InsufficientDataException | InternalException
				| InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException
				| IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public List<Bucket> getAllBuckets() {
		try {
			return minioClient.listBuckets();
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public Optional<Bucket> getBucket(String bucketName) {
		try {
			return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public void removeBucket(String bucketName) {
		try {
			minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public boolean objectExist(String bucketName, String objectName) {
		boolean existFlg = true;
		try {
			minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
		} catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException
				| NoSuchAlgorithmException | ServerException | XmlParserException | IllegalArgumentException | IOException e) {
			existFlg = false;
		}
		return existFlg;
	}

	@Override
	public boolean folderExist(String bucketName, String objectName) {
		boolean existFlg = false;
		try {
			Iterable<Result<Item>> results = minioClient
					.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(objectName).recursive(false).build());
			for (Result<Item> result : results) {
				Item item = result.get();
				if (item.isDir() && objectName.equals(item.objectName())) {
					existFlg = true;
				}
			}
		} catch (Exception e) {
			existFlg = false;
		}
		return existFlg;
	}

	public Iterable<Result<Item>> listObjects(String bucketname, String prefix, boolean recursive) {
		return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketname).prefix(prefix).recursive(recursive).build());
	}
}