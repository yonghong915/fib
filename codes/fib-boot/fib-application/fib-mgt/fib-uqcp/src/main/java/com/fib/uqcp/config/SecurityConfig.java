package com.fib.uqcp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	// private InvalidAuthenticationEntryPoint invalidAuthenticationEntryPoint;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//				// 禁用basic明文验证
//				.httpBasic().disable()
//				// 前后端分离架构不需要csrf保护
//				.csrf(Customizer.withDefaults())
//				// 禁用默认登录页
//				.formLogin().disable()
//				// 禁用默认登出页
//				.logout().disable()
//				// 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
//				//.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(invalidAuthenticationEntryPoint))
//				// 前后端分离是无状态的，不需要session了，直接禁用。
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//						// 允许所有OPTIONS请求
//						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//						// 允许直接访问授权登录接口
//						.requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
//						// 允许 SpringMVC 的默认错误地址匿名访问
//						.requestMatchers("/error").permitAll()
//						// 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
//						// .requestMatchers("/**").hasAnyAuthority("ROLE_USER")
//						// 允许任意请求被已登录用户访问，不检查Authority
//						.anyRequest().authenticated())
//				.authenticationProvider(authenticationProvider())
//				// 加我们自定义的过滤器，替代UsernamePasswordAuthenticationFilter
//				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
		return http.csrf(csrf -> csrf.disable()).build();
	}

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//		// 登录页为/login.html，请求/login路径验证登录信息，成功后重定向到/index.html页
//		httpSecurity.formLogin().loginProcessingUrl("/login").loginPage("/login.html").defaultSuccessUrl("/index.html");
//		// 允许登录页和登录的url不需要验证即可访问，因为那个时候还没有登录信息
//		httpSecurity.authorizeRequests().antMatchers("/login*").permitAll().anyRequest().authenticated();
//		httpSecurity.csrf().disable();
//		return httpSecurity.build();
//	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtTokenOncePerRequestFilter authenticationJwtTokenFilter() {
		return new JwtTokenOncePerRequestFilter();
	}

	@Bean
	UserDetailsService userDetailsService() {
		// 调用 JwtUserDetailService实例执行实际校验
		return username -> userDetailsService.loadUserByUsername(username);
	}

	/**
	 * 调用loadUserByUsername获得UserDetail信息，在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
	 *
	 * @return
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		// DaoAuthenticationProvider 从自定义的 userDetailsService.loadUserByUsername
		// 方法获取UserDetails
		authProvider.setUserDetailsService(userDetailsService());
		// 设置密码编辑器
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * 登录时需要调用AuthenticationManager.authenticate执行一次校验
	 *
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}