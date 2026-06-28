package com.fib.common.bus.base;

import lombok.Data;

/**
 * 报文头
 */
@Data
public class MsgHeader {
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