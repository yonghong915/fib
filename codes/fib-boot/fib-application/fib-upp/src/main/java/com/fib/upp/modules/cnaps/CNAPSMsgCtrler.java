package com.fib.upp.modules.cnaps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upp/cnaps")
public class CNAPSMsgCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CNAPSMsgCtrler.class);

	/**
	 * 来账处理入口
	 */
	@PostMapping(value = "/in")
	public void handleRcvMsg() {
		LOGGER.info("handleRcvMsg");
	}

	/**
	 * 往账处理入口
	 */
	@PostMapping(value = "/out")
	public void handleSndMsg() {
		LOGGER.info("handleSndMsg");
	}
}
