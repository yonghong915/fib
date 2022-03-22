package com.fib.upp.ctrler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 来账公共处理
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-02-24
 */
@RestController
@RequestMapping("/messageIn")
public class MessageInCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageInCtrler.class);

	public void handle() {
		LOGGER.info("");
	}

}
