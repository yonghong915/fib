package com.fib.autoconfigure.minio.service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

/**
 * Minio服务接口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-11 09:42:11
 */
public interface IMinioService {
	/** 桶是否存在 */
	boolean bucketExists(String bucketName);

	/** 创建桶 */
	void createBucket(String bucketName);

	/** 上传文件 */
	void putObject(String bucketName, String objectName, InputStream is);

	/** 上传文件 */
	void uploadObject(String bucketName, String objectName, String fileName);

	/**
	 * 获取文件流
	 * 
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	InputStream getObject(String bucketName, String objectName);

	/**
	 * 获取存储桶策略
	 *
	 * @param bucketname 存储桶名称
	 */
	String getBucketPolicy(String bucketName);

	/** 获取全部bucket */
	List<Bucket> getAllBuckets();

	/**
	 * 根据bucketname获取信息
	 * 
	 * @param bucketName
	 * @return
	 */
	Optional<Bucket> getBucket(String bucketName);

	/**
	 * 根据bucketname删除信息
	 * 
	 * @param bucketName
	 */
	void removeBucket(String bucketName);

	/**
	 * 判断文件是否存在
	 * 
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	boolean objectExist(String bucketName, String objectName);

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	boolean folderExist(String bucketName, String objectName);

	/**
	 * 断点下载
	 * 
	 * @param bucketname
	 * @param objectName
	 * @param offset       起始字节的位置
	 * @param length要读取的长度
	 * @return
	 */
	InputStream getObject(String bucketName, String objectName, long offset, long length);

	/**
	 * 获取路径下文件列表
	 * 
	 * @param bucketname bucket名称
	 * @param prefix     文件名称
	 * @param recursive  是否递归查找，如果是false,就模拟文件夹结构查找
	 * @return
	 */
	Iterable<Result<Item>> listObjects(String bucketname, String prefix, boolean recursive);
}
