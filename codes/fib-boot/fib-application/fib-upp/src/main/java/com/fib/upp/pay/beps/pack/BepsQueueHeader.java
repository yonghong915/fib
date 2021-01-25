package com.fib.upp.pay.beps.pack;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@Data
public class BepsQueueHeader {
	/***/
	private Long pkId;
	/***/
	private String queueType;
	/***/
	private String status;

}
