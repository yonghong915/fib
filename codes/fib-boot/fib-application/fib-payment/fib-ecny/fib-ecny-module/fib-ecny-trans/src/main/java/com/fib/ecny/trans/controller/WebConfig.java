//package com.fib.ecny.trans.controller;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        for (HttpMessageConverter<?> converter : converters) {
//            if(converter instanceof MappingJackson2HttpMessageConverter jsonConverter){
//                ObjectMapper mapper = jsonConverter.getObjectMapper();
//                // 开启根节点包装
//                mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
//                mapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
//            }
//        }
//    }
//}