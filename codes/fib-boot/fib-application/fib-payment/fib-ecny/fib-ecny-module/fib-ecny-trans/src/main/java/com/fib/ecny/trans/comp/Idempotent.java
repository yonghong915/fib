package com.fib.ecny.trans.comp;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * SpEL表达式，用来获取幂等唯一key
     * 示例：
     * #dto.orderNo
     * #header.get('idemToken')
     */
    String key();

    /**
     * 幂等缓存过期时间(秒)
     */
    long expire() default 300;

    /**
     * 幂等类型
     * TOKEN：一次性删除模式（前端表单）
     * BUSINESS：SETNX 业务key模式（回调/MQ）
     */
    IdemType type() default IdemType.TOKEN;

    /**
     * 重复请求提示信息
     */
    String msg() default "请勿重复提交";

    enum IdemType {
        TOKEN, BUSINESS
    }
}
