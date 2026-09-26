package com.fib.ecny.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BizContext implements Serializable {
    private String service;
    private String serialNo;
    private String channelCode;
    private String channelDate;
    private String channelTime;
    private String channelSeqNo;
    private String transCode;
    private String transDate;
    private String transTime;
    private String globalSeq;
    private String lang;
    private String channelAddr;
    private String serverName;
    private String sceneId;
    private Class<?> clazz;
    private Object request;
    private Long duration;

    public BizContext(){}
    public BizContext(Object request){
        //super(request);
        this.request = request;
        this.clazz = request.getClass();
    }

    public <T> void setRequest(T request){
        this.request = request;
        this.clazz = request.getClass();
    }

    public <T> T getRequest(){
        return this.getRequest();
    }

    public void put(String flag, boolean b) {
    }
}
