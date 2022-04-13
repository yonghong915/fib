package com.fib.upp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;
import com.fib.upp.modules.cnaps.service.ICnapsSystemStatusService;

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
		CnapsSystemStatus inCss = new CnapsSystemStatus();
		inCss.setSystemCode(systemCode);
		inCss.setClearBankNo(clearBankNo);
		Opt<CnapsSystemStatus> cssOpt = cnapsSystemStatusService.getCnapsSystemStatus(inCss);
		CnapsSystemStatus css = cssOpt.orElseThrow(() -> new RuntimeException("aaaaa"));
		assertNotNull(css);
	}
}