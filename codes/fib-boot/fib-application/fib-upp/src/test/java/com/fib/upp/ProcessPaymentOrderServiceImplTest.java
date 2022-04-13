package com.fib.upp;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.modules.common.service.ICommonService;
import com.fib.upp.util.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class ProcessPaymentOrderServiceImplTest {

	@Autowired
	private ICommonService processPaymentOrder;

	@Test
	public void testExecute() {
		Map<String, Object> context = new HashMap<>();
		context.put(Constant.FieldKey.TRANS_ID.code(), "1223344555");
		processPaymentOrder.execute(context);
		assertTrue(true);
	}
}