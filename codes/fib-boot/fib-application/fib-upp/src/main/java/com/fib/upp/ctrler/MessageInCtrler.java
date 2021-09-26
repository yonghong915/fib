package com.fib.upp.ctrler;

import java.util.concurrent.Executor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fib.commons.web.ResultRsp;
import com.fib.commons.web.ResultUtil;
import com.fib.core.util.SpringContextUtils;
import com.fib.core.util.StatusCode;
import com.fib.upp.pay.beps.pack.BepsMessagePackRule;
import com.fib.upp.pay.beps.pack.BepsPackUtil;
import com.fib.upp.service.IBepsPackService;
import com.fib.upp.service.rulecheck.vo.RuleCheckVO;

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
	private Logger logger = LoggerFactory.getLogger(MessageInCtrler.class);

	@Autowired
	private IBepsPackService bepsPackService;
	
	@Autowired
	private BepsPackUtil bepsPackUtil;

	@PostMapping(value = "/test")
	public ResultRsp<Object> testValidation(@Validated @RequestBody RuleCheckVO vo) {
		logger.info("MessageInCtrler-->");
		bepsPackService.queryBepsPackRuleList();

		Executor executor = SpringContextUtils.getBean("customAsyncExcecutor");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("thread log.......");
			}
		});

		executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("thread log1111111111.......");
			}
		});

		executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("thread log222222222.......");
			}
		});

		
		BepsMessagePackRule bepsPackRule = bepsPackUtil.getPackRuleByMessageType("beps.121.001.01");
		return ResultUtil.message(StatusCode.SUCCESS);
	}

	@RequestMapping(value = "/handle", method = RequestMethod.POST)
	public String handle(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Enter MessageInCtrler...");
		String requestMessage = request.getParameter("requestMessage");

		boolean duplexFlag = checkMessageDuplex(requestMessage);
		if (duplexFlag) {
			logger.info("业务判重：此报文在系统中已存在【依据通道、发起机构、报文标识号】.");
//			out.write("业务判重：此报文在系统中已存在【依据通道、发起机构、报文标识号】。");
//			out.flush();
//			out.close();
//			return;
			return "业务判重：此报文在系统中已存在【依据通道、发起机构、报文标识号】。";
		}
		return requestMessage;

		// 获取机构

		// 查找原业务，并更新原交流事件状态PE99 CommunicationEvent

		// 大额来账退汇业务，如果找不到原业务当中一笔来贷处理

		// 收到来账报文,调用平台服务之前向人行发送技术回执报文990

		// 核押出错流程直接返回错误信息

		// 执行服务serviceId

	}

	/**
	 * 来账报文判重 // 报文判重处理，使用本次报文中的通道、发起机构、报文标识号判断本次收到的报文在系统中是否已经存在，若存在不做处理，不存在执行后续的逻辑
	 * 
	 * @param requestMessage
	 * @return
	 */
	private boolean checkMessageDuplex(String requestMessage) {
		boolean duplexFlag = false;
		//
		if ("111".equals(requestMessage)) {
			duplexFlag = true;
		}
		return duplexFlag;
	}
	
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
