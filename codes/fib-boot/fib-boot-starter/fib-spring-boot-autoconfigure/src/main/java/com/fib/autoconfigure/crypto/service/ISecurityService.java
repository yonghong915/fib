package com.fib.autoconfigure.crypto.service;

import com.fib.autoconfigure.crypto.annotation.AlgorithmType.Algorithm;
import com.fib.autoconfigure.crypto.dto.Request;
import com.fib.autoconfigure.crypto.dto.RequestHeader;

public interface ISecurityService {

	String queryPrivateKey(String systemCode);

	String queryPublicKey(String systemCode);

	long getCurrTimestamp();

	String getNonce(long timestamp);

	Request buildReq(Object body, Algorithm algorithm);

	String getEncrypBody(Object body, RequestHeader reqHeader);

	String getSecurityKey(RequestHeader reqHeader);
}
