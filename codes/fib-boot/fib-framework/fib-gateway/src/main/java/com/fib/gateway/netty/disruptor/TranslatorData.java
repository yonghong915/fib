package com.fib.gateway.netty.disruptor;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-12
 */
@Data
@ToString
public class TranslatorData implements Serializable {

	private static final long serialVersionUID = 8763561286199081881L;

	private String id;
	private String name;
	private String message; // 传输消息体内容
}
