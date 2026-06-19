package com.fib.common.bus.base;

public class RespEntity {

    public static RespEntity fail() {
        return new RespEntity();
    }

    private Object body;

    public static RespEntity ok(Object detail) {
        RespEntity respEntity = new RespEntity();
        respEntity.setBody(detail);
        return respEntity;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
