package com.fib.upp;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.commons.util.CommUtils;
import com.fib.upp.modules.payment.entity.PaymentPreferInfo;
import com.fib.upp.modules.payment.service.IPaymentPreferInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class PaymentPreferInfoServiceImplTest {

	@Autowired
	IPaymentPreferInfo paymentPreferInfoService;

	@Test
	@Ignore("test")
	public void testSave() {
		PaymentPreferInfo ppi = new PaymentPreferInfo();
		ppi.setPaymentPreferId(Long.valueOf(CommUtils.getRandom("0123456789", 10)));
		ppi.setOrderId(new BigInteger(CommUtils.getRandom("0123456789", 10)));
		ppi.setPaymentMethodTypeId("DEBIT_CARD");
		ppi.setPaymentMethodId("3835006");
		ppi.setTransAmt(new BigDecimal("10023444.01"));
		ppi.setPaymentStatus("PAYMENT_CANCELLED");
		ppi.setAcctNo("6270616601000024376");
		ppi.setAcctName("普凤英");
		ppi.setAcctType("0");
		ppi.setSysCode("BEPS");
		ppi.setCustPostscript("测试");
		ppi.setBankPostscript("银行测试");
		ppi.setBankCode("323653010015");
		ppi.setBankName("323653010015银行");
		ppi.setTradeOrgId("0101");
		ppi.setDcFlag("D");
		ppi.setRouteId("CNAPS_COME_LEND");
		boolean flag = paymentPreferInfoService.save(ppi);
		assertEquals(true, flag);
	}

	@Test
	public void testQuery() {
		PaymentPreferInfo ppi = paymentPreferInfoService.getById(Long.valueOf("1251370276"));
		assertNotNull(ppi);
	}
}
