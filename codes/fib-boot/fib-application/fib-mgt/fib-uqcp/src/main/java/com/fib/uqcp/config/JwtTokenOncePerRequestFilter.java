package com.fib.uqcp.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 每次请求的 Security 过滤类。执行jwt有效性检查，如果失败，不会设置 SecurityContextHolder 信息，会进入
 * AuthenticationEntryPoint
 */
public class JwtTokenOncePerRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = jwtTokenProvider.resolveToken(request);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);

				if (auth != null) {
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication!", e);
		}
		filterChain.doFilter(request, response);
	}
}