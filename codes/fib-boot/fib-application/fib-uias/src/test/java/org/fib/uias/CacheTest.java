package org.fib.uias;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.uias.UiasApplication;
import com.fib.uias.entity.UserEntity;
import com.fib.uias.service.IUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UiasApplication.class)
@EnableCaching
public class CacheTest {
	@Autowired
	private IUserService userServiceImpl;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void getUserByIdTest() throws Exception {
		UserEntity user = userServiceImpl.getUser("12233");
		System.out.println(user.getPkId());
		assertNotNull(user);

	}
}
