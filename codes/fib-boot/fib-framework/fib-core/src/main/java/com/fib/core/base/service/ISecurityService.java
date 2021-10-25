package com.fib.core.base.service;

public interface ISecurityService {

	String queryPrivateKey(String systemCode);
	
	String queryPublicKey(String systemCode);
}
