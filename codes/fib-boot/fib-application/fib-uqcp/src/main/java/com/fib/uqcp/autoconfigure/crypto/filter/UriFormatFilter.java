package com.fib.uqcp.autoconfigure.crypto.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fib.autoconfigure.crypto.request.RequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

public class UriFormatFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = httpServletRequest.getRequestURI();
		String newUri = uri.replace("//", "/");
		httpServletRequest = new HttpServletRequestWrapper(httpServletRequest) {
			@Override
			public String getRequestURI() {
				return newUri;
			}
		};
		ServletRequest requestWrapper = new RequestWrapper(httpServletRequest);
		filterChain.doFilter(requestWrapper, httpServletResponse);
	}

}
