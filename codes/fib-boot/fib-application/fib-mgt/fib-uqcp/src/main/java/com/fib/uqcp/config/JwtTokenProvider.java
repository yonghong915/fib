package com.fib.uqcp.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service("jwtTokenProvider")
public class JwtTokenProvider {

	public String resolveToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean validateToken(String token) {
		// TODO Auto-generated method stub
		return false;
	}

	public Authentication getAuthentication(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
