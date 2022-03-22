package com.fib.upp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fib.upp.ctrler.MessageInCtrler;
import com.fib.upp.service.IBepsPackService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebCtrlerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebCtrlerTest.class);

	private MockMvc mockMvc;

	@MockBean
	private IBepsPackService bepsPackService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new MessageInCtrler()).build();
	}

	@Test
	public void queryOk() throws Exception {
		String content = "";
		String url = "/rest/animals";

		when(bepsPackService.packBepsMessage()).thenReturn(1);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.request(HttpMethod.POST, url).contentType("application/json")
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())// 返回状态200为成功
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("pig"))// 返回属性name为pig
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.animalList[0].type").value(5))// 返回属性type需要为5
				.andDo(print()).andReturn();
		LOGGER.info(result.getResponse().getContentAsString());
	}
}