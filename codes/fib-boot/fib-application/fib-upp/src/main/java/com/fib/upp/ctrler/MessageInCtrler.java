package com.fib.upp.ctrler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageInCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageInCtrler.class);

	/**
	 * 往账处理入口
	 */
	@PostMapping(value = "/handleOut")
	public Map<String, String> handleOut() {
		LOGGER.info("");
		Map<String, String> rtnMap = new HashMap<>();
		rtnMap.put("name", "pig");
		rtnMap.put("type", "5");
		return rtnMap;
	}

	/**
	 * 来账处理入口
	 */
	@PostMapping(value = "/handleIn")
	public void handleIn() {
		LOGGER.info("");
	}
}
