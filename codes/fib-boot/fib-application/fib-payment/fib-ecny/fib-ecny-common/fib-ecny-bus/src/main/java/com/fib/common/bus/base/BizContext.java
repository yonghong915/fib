package com.fib.common.bus.base;

import lombok.Data;

@Data
public class BizContext<T> {
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
    private T request;

    public void put(String flag, boolean b) {
    }
}
