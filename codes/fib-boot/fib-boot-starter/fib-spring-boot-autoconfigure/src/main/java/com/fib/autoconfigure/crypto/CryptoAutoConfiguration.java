package com.fib.autoconfigure.crypto;

import org.apache.dubbo.rpc.model.ScopeModelUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.crypto.config.CryptoProperties;
import com.fib.autoconfigure.crypto.service.ISecurityService;
import com.fib.autoconfigure.crypto.service.impl.SecurityServiceImpl;

/**
 * API接口安全自动配置类
 * <p>
 * 实现方式:<br/>
 * 1.发送方加密处理流程<br/>
 * 1)、生成签名<br/>
 * a.将返回对象转换成json字符串jsonStr<br/>
 * b.生成当前时间戳timestamp、nonce数据(ip+当前时间戳+8位随机数)<br/>
 * c.使用SM3摘要加密算法对当前时间戳、nonce数据、JSON字符串生成摘要,SM3(timestamp+nonce+bodyStr)<br/>
 * d.使用SM2公钥对SM3摘要数据生成签名数据signature<br/>
 * 2)、加密请求报文<br/>
 * a.动态生成一个16位随机数，作为SM4秘钥securityKey<br/>
 * b.使用securityKey秘钥对jsonStr字符串用SM4对称加密算法加密生成bodyStr<br/>
 * 
 * 3)、加密SM4密钥<br/>
 * a.用Base64工具将SM4秘钥securityKey转码为密钥字符串securityKeyStr<br/>
 * b.使用SM2公钥对securityKeyStr加密生成加密字符串SM2KeySecert<br/>
 * 4)、构建http post请求<br/>
 * a.将签名signature放入http请求头的Authentication<br/>
 * b.将SM2KeySecert放入http请求头的SecurityKey<br/>
 * c.将当前时间戳放入http请求头的TimeStamp<br/>
 * d.将nonce串放入http请求头的Nonce<br/>
 * e.将bodyStr以data:bodyStr形式放入http body中<br/>
 * 
 * 5)处理http请求返回结果，使用SecurityKey解密返回结果
 * 
 * 2.接收方解密处理流程<br/>
 * 1)、检验时间:从http请求头TimeStamp中获取时间戳，校验与服务器当前时间是否超过5分钟<br/>
 * 2)、获取SecurityKey:从http请求头SecurityKey中获取SecurityKey字符串,使用SM2私钥对SecurityKey做解密操作获取原始对称加密密钥<br/>
 * 3)、获取bodyStr:从http
 * body中获取请求报文体httpBody，使用SM4对称加密密钥SecurityKey做解密操作取得bodyStr<br/>
 * 4)、验签：使用SM3摘要加密算法对获取的时间戳、nonce数据、bodyStr字符串生成摘要,SM3(timestamp+nonce+bodyStr)<br/>
 * 获取http请求头Authentication中的signature字符串<br/>
 * 使用SM2对生成对摘要进行验签<br/>
 * </p>
 * <p>
 * 使用:<br/>
 * 1.启动crypto使用<br/>
 * fib.crypto.enable=true<br/>
 * 2.启动类中加入 @EnableSecurity注解;<br/>
 * 3.在API接口方法中加 @Encrypt、@Decrypt
 * </p>
 * 
 * @author fangyh
 * @version 1.0
 * @date 2024-01-31 11:11:21
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ ScopeModelUtil.class })
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoAutoConfiguration {
	@Bean
	ISecurityService securityService() {
		return new SecurityServiceImpl();
	}
}