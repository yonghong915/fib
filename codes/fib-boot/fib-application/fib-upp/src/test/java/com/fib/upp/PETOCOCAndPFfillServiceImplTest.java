package com.fib.upp;

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
public class PETOCOCAndPFfillServiceImplTest {
	@Autowired
	private ICommonService pETOCOCAndPFfill;

	@Test
	public void testExecute() {
		Map<String, Object> context = new HashMap<>();
		context.put(Constant.FieldKey.SYS_CODE.code(), Constant.SysCode.HVPS.code());
		context.put(Constant.FieldKey.TRANS_ID.code(), "1223344555");
		pETOCOCAndPFfill.execute(context);
	}
}
