package org.fib.uias;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.uias.UiasApplication;
import com.fib.uias.entity.ErrorCodeEntity;
import com.fib.uias.service.IErrorCodeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UiasApplication.class)
public class ErrorCodeServiceTest {

	@Autowired
	private IErrorCodeService errorCodeService;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetErrorCodeList() {
		ErrorCodeEntity entity = new ErrorCodeEntity();
		List<ErrorCodeEntity> list = errorCodeService.selectList(entity);
		for (ErrorCodeEntity errorCode : list) {
			System.out.println(errorCode.getErrorDesc());
		}
	}
}
