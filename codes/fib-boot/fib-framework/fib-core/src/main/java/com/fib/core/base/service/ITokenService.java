package com.fib.core.base.service;

import javax.servlet.http.HttpServletRequest;

public interface ITokenService {

	void checkToken(HttpServletRequest request);

}
