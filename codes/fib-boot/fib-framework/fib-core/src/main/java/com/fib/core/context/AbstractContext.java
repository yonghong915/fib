package com.fib.core.context;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.map.MapUtil;

public abstract class AbstractContext implements Context {

	private Map<String, Object> dataMap;
	private Timestamp timestamp;

	protected AbstractContext(String key) {
		dataMap = new HashMap<>();
		timestamp = new Timestamp(System.currentTimeMillis());
	}

	protected AbstractContext() {
	}

	@Override
	public void setData(String key, Object value) {
		if (null == value) {
			this.dataMap.remove(key);
		} else {
			this.dataMap.put(key, value);
		}
	}

	@Override
	public Object getData(String key) {
		return this.dataMap.get(key);
	}

	@Override
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap.putAll(dataMap);
	}

	@Override
	public Map<String, Object> getDataMap() {
		return this.dataMap;
	}

	@Override
	public Timestamp getTimestamp() {
		return timestamp;
	}

	protected String getStr(String key) {
		return MapUtil.getStr(dataMap, key);
	}

	protected Integer getInteger(String key) {
		return MapUtil.getInt(dataMap, key);
	}
}