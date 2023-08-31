package com.fib.core.context;

import java.sql.Timestamp;
import java.util.Map;

public interface Context {
	void setData(String key, Object value);

	Object getData(String key);

	void setDataMap(Map<String, Object> paramMap);

	Map<String, Object> getDataMap();

	Timestamp getTimestamp();
}
