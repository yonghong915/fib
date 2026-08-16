package com.fib.ecny.base.service;

import lombok.Data;

/**
 * 报文头
 */
@Data
public class MsgHeader {
    private String origSender;
    private String origReceiver;
    /**
     *
     */
    private String msgId;

    /**
     *
     */
    private String mesgType;

    /**
     *
     */
    private String origSendDateTime;

    private String lang;
}