package com.fib.upp;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.core.base.dto.BaseDTO;
import com.fib.core.util.SpringContextUtils;
import com.fib.upp.service.IBepsPackService;
import com.fib.upp.service.rulecheck.IRuleCheckService;
import com.fib.upp.service.rulecheck.dto.BankOrgDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class RuleCheckServiceTest {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IBepsPackService bepsPackService;

	@Test
	public void checkExternalMsg() throws Exception {
		List<String> serviceNameList = getCheckServicesByPath();

		BankOrgDTO dto = new BankOrgDTO();
		dto.setChannel("0201");
		dto.setChannelDate(new Date());
		dto.setChannelSerialNo("1233455667");

		dto.setSystemCode("HVPS");
		dto.setTransactionId("903344383494");
		IRuleCheckService<BaseDTO> ruleCheckService = null;
		boolean checkFlag = false;
		try {
			for (String serviceName : serviceNameList) {
				ruleCheckService = SpringContextUtils.getBean(serviceName);
				checkFlag = ruleCheckService.check(dto);
				if (checkFlag) {
					logger.info("call service {},rule check passed.", serviceName);
				} else {
					logger.info("call service {},rule check not passed.", serviceName);
				}
			}
		} catch (Exception e) {
			logger.error("checkExternalMsg error,", e);
		}

		// getCheckServicesByProductId(productId,checkAttrName);
		bepsPackService.queryBepsPackRuleList();
		assertTrue(true);
	}

	private List<String> getCheckServicesByPath() {
		// checkAttrName = "RuleCheckFunc";
		// systemCode HVPS BEPS
		List<String> serviceNameList = new ArrayList<>();
		serviceNameList.add("checkBankInnerOrgService");
		serviceNameList.add("checkCnapsSystemStatusService");
		return serviceNameList;
	}
}
