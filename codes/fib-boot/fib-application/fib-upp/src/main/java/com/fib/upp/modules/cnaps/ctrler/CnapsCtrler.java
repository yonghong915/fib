package com.fib.upp.modules.cnaps.ctrler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.commons.web.ResultRsp;
import com.fib.commons.web.ResultUtil;

/**
 * 人行来账报文
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-11 15:18:57
 */
@RestController
@RequestMapping("/cnaps")
public class CnapsCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CnapsCtrler.class);

	/**
	 * 
	 * @return
	 */
	@PostMapping("/execute")
	public ResultRsp<Object> execute() {
		LOGGER.info("execute");
		return ResultUtil.message(null);
	}
}
