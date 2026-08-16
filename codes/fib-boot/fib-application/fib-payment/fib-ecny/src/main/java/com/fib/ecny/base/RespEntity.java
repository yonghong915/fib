package com.fib.ecny.base;

import com.fib.ecny.base.service.EcnyErrorCode;
import lombok.Data;

@Data
public class RespEntity<T> {

    private String respType;
    private String respCode;
    private String respDesc;
    private String respProCd;
    private UwapResponse<T> respData;
    private Boolean needErrorCodeMapping = Boolean.FALSE;
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

    public Object setFailed(EcnyErrorCode ecnyErrorCode) {
        return null;
    }

    public boolean unsucc() {
        return true;
    }

    public boolean okey() {
        return true;
    }
}
