package com.fib.upp.xml;


import org.dom4j.Document;
import org.dom4j.io.SAXReader;


public class ModuleCacheManager extends AbstractCacheTemplate {
	private ModuleCacheManager() {
		//
	}

	public static ModuleCacheManager getInstance() {
		return SingletonFactory.singletonInstance;
	}

	private static class SingletonFactory {
		private static ModuleCacheManager singletonInstance = new ModuleCacheManager();
	}

	protected Object load(String key) {
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

	@Override
	protected Object load(String key, String sysCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
