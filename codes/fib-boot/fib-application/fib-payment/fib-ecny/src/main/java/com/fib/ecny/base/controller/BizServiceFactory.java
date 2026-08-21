package com.fib.ecny.base.controller;

import com.fib.ecny.common.exception.BizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
//@Component
public class BizServiceFactory {
    /**
     * Spring自动注入所有IBizService实现类
     */
    @Resource
    private Map<String, IDisputeService> disputeServiceMap;

    /**
     * 本地缓存：bizCode -> Spring Bean实例，ConcurrentHashMap保证并发安全
     */
    private final Map<String, IDisputeService> serviceCache = new ConcurrentHashMap<>();

    /**
     * 项目启动完成自动加载所有IBizService实现
     */

    public void run(ApplicationArguments args) {
        refreshCache();
    }

    /**
     * 手动刷新缓存，动态注册Bean之后调用此方法
     */
    public void refreshCache() {
        serviceCache.clear();
        // 获取容器中全部IBizService的Bean
        disputeServiceMap.forEach((key, disputeService) -> {
            DisputeServiceType anno = disputeService.getClass().getAnnotation(DisputeServiceType.class);
            if (Objects.isNull(anno)) {
                log.warn("IBizService实现类{}未添加@BizServiceCode注解，跳过缓存", disputeService.getClass().getName());
                serviceCache.put(key, disputeService);
            } else {
                String[] serviceBeans = anno.value();
                Arrays.stream(serviceBeans).forEach((serviceBean) -> {
                    serviceCache.put(serviceBean, disputeService);
                });
            }
        });
    }

    /**
     * 根据业务码获取处理器
     *
     * @param bizCode 业务编码
     * @return IBizService 实现实例
     */
    public IDisputeService getService(String bizCode) {
        IDisputeService service = serviceCache.get(bizCode);
        if (Objects.isNull(service)) {
            throw new BizException("未找到业务处理器，bizCode:" + bizCode);
        }
        return service;
    }

    /**
     * 获取全部缓存
     */
    public Map<String, IDisputeService> getAllService() {
        return new ConcurrentHashMap<>(serviceCache);
    }
}
