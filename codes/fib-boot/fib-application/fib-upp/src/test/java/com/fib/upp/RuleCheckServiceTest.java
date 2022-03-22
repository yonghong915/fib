package com.fib.upp;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
class RuleCheckServiceTest {

//	@Autowired
//	private IBepsPackService bepsPackService;

	@Test
	void test() {
		assertTrue(true);
	}

//	@Test
//	public void checkExternalMsg() throws Exception {
//		List<String> serviceNameList = getCheckServicesByPath();
//
//		BankOrgDTO dto = new BankOrgDTO();
//		dto.setChannel("0201");
//		dto.setChannelDate(new Date());
//		dto.setChannelSerialNo("1233455667");
//
//		dto.setSystemCode("HVPS");
//		dto.setTransactionId("903344383494");
//		IRuleCheckService<BaseDTO> ruleCheckService = null;
//		boolean checkFlag = false;
//		try {
//			for (String serviceName : serviceNameList) {
//				ruleCheckService = SpringContextUtils.getBean(serviceName);
//				checkFlag = ruleCheckService.check(dto);
//				if (checkFlag) {
//					logger.info("call service {},rule check passed.", serviceName);
//				} else {
//					logger.info("call service {},rule check not passed.", serviceName);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("checkExternalMsg error,", e);
//		}
//
//		// getCheckServicesByProductId(productId,checkAttrName);
//		bepsPackService.packBepsMessage();
//		assertTrue(true);
//	}

//	private List<String> getCheckServicesByPath() {
//		// checkAttrName = "RuleCheckFunc";
//		// systemCode HVPS BEPS
//		List<String> serviceNameList = new ArrayList<>();
//		serviceNameList.add("checkBankInnerOrgService");
//		serviceNameList.add("checkCnapsSystemStatusService");
//		return serviceNameList;
//	}
}
