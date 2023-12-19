package com.fib.core.base.service;

import jakarta.servlet.http.HttpServletRequest;

public interface ITokenService {

	void checkToken(HttpServletRequest request);

}
