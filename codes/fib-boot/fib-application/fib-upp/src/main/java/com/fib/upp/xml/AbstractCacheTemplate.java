package com.fib.upp.xml;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public abstract class AbstractCacheTemplate {
	protected abstract Object load(String key) throws Exception;

	protected abstract Object load(String key, String sysCode) throws Exception;

	// 交易数据模板缓存器
	private Cache<String, Object> modualCache = cacheManager();

	public Cache<String, Object> cacheManager() {
		// Caffeine配置
		Cache<String, Object> caffeine = Caffeine.newBuilder().initialCapacity(100).maximumSize(1000)
				// 最后一次写入后经过固定时间过期
				.expireAfterWrite(10, TimeUnit.SECONDS).build();
		return caffeine;
	}

	/**
	 * 得到交易数据模板
	 */
	public Object getModule(String key) {
		return modualCache.get(key, new Function<String, Object>() {
			@Override
			public Object apply(String aLong) {
				return load1(aLong);
			}
		});
	}

	private Object load1(String key) {
		SAXReader reader = new SAXReader();
		Document modul = null;

		try {

			modul = reader.read(key);
		} catch (Exception e) {
			e.printStackTrace();
			// LogUtils.log(new
			// StringBuffer().append("加载模板").append(key).append("失败").toString(),e);
			// throw new Exception(new
			// StringBuffer().append("加载模板").append(key).append("失败").toString());
		}
		return modul;
	}
}
