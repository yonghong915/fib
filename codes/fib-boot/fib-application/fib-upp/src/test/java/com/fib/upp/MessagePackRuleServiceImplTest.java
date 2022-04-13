package com.fib.upp;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.modules.beps.entity.MessagePackRule;
import com.fib.upp.modules.beps.service.IMessagePackRuleService;

import cn.hutool.core.lang.Opt;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class MessagePackRuleServiceImplTest {

	@Autowired
	IMessagePackRuleService messagePackRuleService;

	@Test
	public void testGetMessagePackRule() {
		String messageTypeCode = "beps.121.001.01";
		MessagePackRule rul = new MessagePackRule();
		rul.setMessageTypeCode(messageTypeCode);
		Opt<MessagePackRule> mprOpt = messagePackRuleService.getMessagePackRule(rul);
		MessagePackRule mpr = mprOpt.orElseThrow(() -> new RuntimeException("aaaaa"));
		assertTrue(mpr.sendClearingBank());
	}

	@Test
	public void testQueryMessagePackRuleList() {
		List<MessagePackRule> mprList = messagePackRuleService.queryMessagePackRuleList();
		for (MessagePackRule mpr : mprList) {
			System.out.println(mpr.sendClearingBank());
		}
		assertTrue(true);
	}
}