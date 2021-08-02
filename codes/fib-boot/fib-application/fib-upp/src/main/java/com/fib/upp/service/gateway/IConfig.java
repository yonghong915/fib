package com.fib.upp.service.gateway;

public interface IConfig {
	Object load(String fileName, Object... params);
}
