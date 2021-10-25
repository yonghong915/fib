package com.fib.core.base.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import com.fib.core.base.service.ITokenService;

import cn.hutool.core.util.StrUtil;

@Service("tokenService")
public class TokenServiceImpl implements ITokenService {
	private static final String TOKEN_KEY = "tokenKey";

	@Override
	public void checkToken(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_KEY);
		if (StrUtil.isBlank(token)) {
			token = request.getParameter(TOKEN_KEY);
		}

		if (StrUtil.isBlank(token)) {
			// throw new BusinessException(StatusCode.INVALID_TOKEN);
		}

		// TODO
		// 校验token
		boolean checkFlag = false;
		if (!checkFlag) {
			// throw new BusinessException(StatusCode.INVALID_TOKEN);
		}
		// jedisUtil.exists(token);
		// jedisUtil.del(token)

	}

}
