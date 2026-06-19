package com.fib.common.bus.base;

public class BizContext<T> {
    private String service;
    private String channelDate;
    private String channelTime;
    private String channelSeqNo;
    private String transCode;
    private String transDate;
    private String transTime;
    private String globalSeq;
    private UwapRequest<T> request;

    public void put(String flag, boolean b) {
    }

    public UwapRequest<T> getRequest() {
        return request;
    }

    public void setRequest(UwapRequest<T> request) {
        this.request = request;
    }

    public String getGlobalSeq() {
        return globalSeq;
    }

    public void setGlobalSeq(String globalSeq) {
        this.globalSeq = globalSeq;
    }

    public String getChannelSeqNo() {
        return channelSeqNo;
    }

    public void setChannelSeqNo(String channelSeqNo) {
        this.channelSeqNo = channelSeqNo;
    }

    public String getChannelDate() {
        return channelDate;
    }

    public void setChannelDate(String channelDate) {
        this.channelDate = channelDate;
    }

    public String getChannelTime() {
        return channelTime;
    }

    public void setChannelTime(String channelTime) {
        this.channelTime = channelTime;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
