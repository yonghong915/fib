package com.fib.upp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.entity.cnaps.CnapsSystemStatus;
import com.fib.upp.service.ICnapsSystemStatusService;

import cn.hutool.core.lang.Opt;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class CnapsSystemStatusServiceImplTest {

	@Autowired
	ICnapsSystemStatusService cnapsSystemStatusService;

	@Test
	public void testGetMessagePackRule() {
		String systemCode = "HVPS";
		String clearBankNo = "313651071504";
		Opt<CnapsSystemStatus> cssOpt = cnapsSystemStatusService.getCnapsSystemStatus(systemCode, clearBankNo);
		CnapsSystemStatus css = cssOpt.orElseThrow(() -> new RuntimeException("aaaaa"));
		assertNotNull(css);
	}
}