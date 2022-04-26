package com.fib.upp;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.modules.cnaps.entity.CnapsSystemStatus;
import com.fib.upp.modules.cnaps.service.ICnapsSystemStatusService;
import com.fib.upp.modules.common.service.ServiceDispatcher;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class CnapsSystemStatusServiceImplTest {

	@Autowired
	ICnapsSystemStatusService cnapsSystemStatusService;

	@Test
	@Ignore("test")
	public void testGetMessagePackRule() {
		String systemCode = "HVPS";
		String clearBankNo = "313651071504";
		CnapsSystemStatus inCss = new CnapsSystemStatus();
		inCss.setSysCode(systemCode);
		inCss.setClearBankCode(clearBankNo);
		Opt<CnapsSystemStatus> cssOpt = cnapsSystemStatusService.getCnapsSystemStatus(inCss);
		CnapsSystemStatus css = cssOpt.orElseThrow(() -> new RuntimeException("aaaaa"));
		assertNotNull(css);
	}

	@Test
	public void testCnapsSysStatus() {
		String serviceName = "updateCnapsSystemStatus";
		Map<String, Object> paramMap = MapUtil.newHashMap();

		Map<String, Object> extMap = MapUtil.newHashMap();
		extMap.put("sysCode", Constant.SysCode.HVPS.code());
		extMap.put("origSysDate", "2022-04-24");
		extMap.put("origSysStatus", "10");
		extMap.put("clearBankCode", "313651071504");
		extMap.put("currentSysDate", LocalDateTimeUtil.formatNormal(LocalDate.now()));
		extMap.put("currentSysStatus", "20");
		extMap.put("holidayFlag", "HF01");
		extMap.put("specialWorkDayFlag", "SF01");
		extMap.put("nextSysDate", "2022-04-26");
		extMap.put("loginOperType", "OT00");
		extMap.put("processCode", "CU1I0000");
		extMap.put("productId", "X021cm801");
		paramMap.put("extMap", extMap);
		Map<String, Object> rtnMap = ServiceDispatcher.runSync(serviceName, paramMap);
		System.out.println(rtnMap);
		assertNotNull(rtnMap);
	}
}