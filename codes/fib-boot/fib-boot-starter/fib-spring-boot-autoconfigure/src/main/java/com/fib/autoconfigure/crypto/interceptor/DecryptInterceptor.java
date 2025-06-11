package com.fib.autoconfigure.crypto.interceptor;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fib.autoconfigure.crypto.annotation.Encrypt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DecryptInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecryptInterceptor.class);
	// @Autowired
	// private EncryptProperties encryptProperties;

	/**
	 * Controller之前执行
	 * preHandle：拦截于请求刚进入时，进行判断，需要boolean返回值，如果返回true将继续执行，如果返回false，将不进行执行。一般用于登录校验
	 * 1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。也就是说，请求不继续往下传了，直接沿着来的链往回跑。
	 * 2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运行所有拦截器的postHandle方法,
	 * 完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info("preHandle.....");
		try {
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();
				if (method.isAnnotationPresent(Encrypt.class)) {
					// 拦截判断是否黑名单
				}

				// RequireLogin annotation = method.getMethodAnnotation(RequireLogin.class);
//				if (method.hasMethodAnnotation(Decrypt.class)) {
//					// 需要对数据进行加密解密
//					// 1.对application/json类型
//					String contentType = request.getContentType();
//					if (contentType == null && !"GET".equals(request.getMethod())) {
//						// 请求不通过，返回错误信息给客户端
//						responseResult(response, response.getWriter(), TResponse.FAIL("Decrypt failed"));
//						return false;
//					}
//					String requestBody = null;
//					boolean shouldEncrypt = false;
//
//					if ((contentType != null && StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
//							|| "GET".equals(request.getMethod())) {
//						// 1.application/x-www-form-urlencoded 支持参数在body或者在param
//						shouldEncrypt = true;
//						requestBody = convertFormToString(request);
//						if (requestBody == null || "{}".equals(requestBody)) {
//							requestBody = URLDecoder.decode(convertInputStreamToString(request.getInputStream()), "UTF-8");
//							List<String> uriToList = Stream.of(requestBody.split("&")).map(elem -> new String(elem)).collect(Collectors.toList());
//
//							Map<String, String> uriToListToMap = new HashMap<>();
//
//							for (String individualElement : uriToList) {
//								if (individualElement.split("=")[0] != null && !"".equals(individualElement.split("=")[0])) {
//									uriToListToMap.put(individualElement.split("=")[0],
//											individualElement.substring(individualElement.split("=")[0].length() + 1));
//								}
//
//							}
//							requestBody = JSONObject.toJSONString(uriToListToMap);
//						}
//
//					} else if (StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_JSON_VALUE)) {
//						// application/json 支持加密参数在body
//						shouldEncrypt = true;
//						requestBody = convertInputStreamToString(request.getInputStream());
//
//					}
//
//					if (requestBody == null || "{}".equals(requestBody) || !shouldEncrypt) {
//						return true;
//					} else {
//
//						String result = decodeApi(JSON.parseObject(requestBody, StdRequestApi.class), encryptProperties.getPrivateKey());
//						if (result == null) {
//							// 请求不通过，返回错误信息给客户端
//							responseResult(response, response.getWriter(), TResponse.FAIL("Decrypt failed"));
//							return false;
//						}
//						JSONObject jasonObject = JSONObject.parseObject(result);
//						Map map = (Map) jasonObject;
//						if (request instanceof RequestWrapper) {
//							RequestWrapper requestWrapper = (RequestWrapper) request;
//							requestWrapper.setBody(result);
//							requestWrapper.addAllParameters(map);
//							// requestWrapper = new RequestWrapper(request, map, result);
//							return true;
//						}
//					}
//				} else {
//					String contentType = request.getContentType();
//					if (contentType != null && contentType.length() > 0
//							&& StringUtils.substringMatch(contentType, 0, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
//						// 1.application/x-www-form-urlencoded 支持参数在body或者在param
//						String requestBody = convertFormToString(request);
//						if (requestBody == null || "{}".equals(requestBody)) {
//							// 把流数据放进param中,不解密
//							requestBody = URLDecoder.decode(convertInputStreamToString(request.getInputStream()), "UTF-8");
//							List<String> uriToList = Stream.of(requestBody.split("&")).map(elem -> new String(elem)).collect(Collectors.toList());
//
//							Map<String, Object> uriToListToMap = new HashMap<>();
//
//							for (String individualElement : uriToList) {
//								if (individualElement.split("=")[0] != null && !"".equals(individualElement.split("=")[0])) {
//									uriToListToMap.put(individualElement.split("=")[0],
//											individualElement.substring(individualElement.split("=")[0].length() + 1));
//								}
//							}
//							if (request instanceof RequestWrapper) {
//								RequestWrapper requestWrapper = (RequestWrapper) request;
//								requestWrapper.setBody(requestBody);
//								requestWrapper.addAllParameters(uriToListToMap);
//								return true;
//							}
//						}
//					}
//				}
//
//				return true;
//			}
//			return true;
			}
		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage() + "异常地址：" + request.getServletPath());
//			responseResult(response, response.getWriter(), TResponse.FAIL("Decrypt failed"));
//			return false;

		}
		return true;
	}

//	/**
//	 * 返回信息给客户端
//	 *
//	 * @param response
//	 * @param tResponse
//	 */
//	private void responseResult(HttpServletResponse response, TResponse tResponse) throws IOException {
//		response.setContentType(HttpConstant.CONTENT_TYPE_JSON);
//		String json = JSONObject.toJSONString(tResponse);
//		PrintWriter out = response.getWriter();
//		out.print(json);
//		out.flush();
//		out.close();
//	}
//
//	private String convertFormToString(HttpServletRequest request) {
//		Map<String, String> result = new HashMap<>(8);
//		Enumeration<String> parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//			String name = parameterNames.nextElement();
//			result.put(name, request.getParameter(name));
//		}
//		try {
//			return JSON.toJSONString(result);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(e);
//		}
//	}
//
//	private String convertInputStreamToString(InputStream inputStream) throws IOException {
//		return StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
//	}
//
//	public String decodeApi(StdRequestApi stdRequestApi, String apiPrivateKey) {
//		try {
//			// 1.rsa解密
//			// 2.AES验签
//			// 3.AES解密
//			return deData;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 返回信息给客户端
//	 *
//	 * @param response
//	 * @param out
//	 * @param tResponse
//	 */
//	private void responseResult(HttpServletResponse response, PrintWriter out, TResponse tResponse) {
//		// response.setContentType(HttpConstant.CONTENT_TYPE_JSON);
//		// String json = JSONObject.toJSONString(tResponse);
//		// out.print(json);
//		out.flush();
//		out.close();
//	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
	}
}
