package com.fib.upp.cnaps2.messagebean.recv.req;

import com.fib.upp.service.gateway.message.bean.MessageBean;

import java.math.BigDecimal;
import java.util.*;

/**
 * 报文头
 */
public class MsgHeader extends MessageBean{

    /**起始标识*/
    private String beginFlag;
    public String getBeginFlag(){
        return beginFlag;
    }

    public void setBeginFlag(String beginFlag){
        this.beginFlag = beginFlag;
    }
    /**版本号*/
    private String versionID;
    public String getVersionID(){
        return versionID;
    }

    public void setVersionID(String versionID){
        this.versionID = versionID;
    }
    /**报文发起人*/
    private String origSender;
    public String getOrigSender(){
        return origSender;
    }

    public void setOrigSender(String origSender){
        this.origSender = origSender;
    }
    /**发送系统号*/
    private String origSenderSID;
    public String getOrigSenderSID(){
        return origSenderSID;
    }

    public void setOrigSenderSID(String origSenderSID){
        this.origSenderSID = origSenderSID;
    }
    /**报文接收人*/
    private String origReceiver;
    public String getOrigReceiver(){
        return origReceiver;
    }

    public void setOrigReceiver(String origReceiver){
        this.origReceiver = origReceiver;
    }
    /**接收系统号*/
    private String origReceiverSID;
    public String getOrigReceiverSID(){
        return origReceiverSID;
    }

    public void setOrigReceiverSID(String origReceiverSID){
        this.origReceiverSID = origReceiverSID;
    }
    /**报文发起日期*/
    private String origSendDate;
    public String getOrigSendDate(){
        return origSendDate;
    }

    public void setOrigSendDate(String origSendDate){
        this.origSendDate = origSendDate;
    }
    /**报文发起时间*/
    private String origSendTime;
    public String getOrigSendTime(){
        return origSendTime;
    }

    public void setOrigSendTime(String origSendTime){
        this.origSendTime = origSendTime;
    }
    /**格式类型*/
    private String structType;
    public String getStructType(){
        return structType;
    }

    public void setStructType(String structType){
        this.structType = structType;
    }
    /**报文类型代码*/
    private String mesgType;
    public String getMesgType(){
        return mesgType;
    }

    public void setMesgType(String mesgType){
        this.mesgType = mesgType;
    }
    /**通信级标识号*/
    private String mesgID;
    public String getMesgID(){
        return mesgID;
    }

    public void setMesgID(String mesgID){
        this.mesgID = mesgID;
    }
    /**通信级参考号*/
    private String mesgRefID;
    public String getMesgRefID(){
        return mesgRefID;
    }

    public void setMesgRefID(String mesgRefID){
        this.mesgRefID = mesgRefID;
    }
    /**报文优先级*/
    private String mesgPriority;
    public String getMesgPriority(){
        return mesgPriority;
    }

    public void setMesgPriority(String mesgPriority){
        this.mesgPriority = mesgPriority;
    }
    /**报文传输方向*/
    private String mesgDirection;
    public String getMesgDirection(){
        return mesgDirection;
    }

    public void setMesgDirection(String mesgDirection){
        this.mesgDirection = mesgDirection;
    }
    /**保留域*/
    private String reserve;
    public String getReserve(){
        return reserve;
    }

    public void setReserve(String reserve){
        this.reserve = reserve;
    }
    /**结束标识*/
    private String endFlag;
    public String getEndFlag(){
        return endFlag;
    }

    public void setEndFlag(String endFlag){
        this.endFlag = endFlag;
    }
}