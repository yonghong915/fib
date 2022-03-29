package com.fib.upp;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fib.autoconfigure.ruleengine.IRuleService;
import com.fib.upp.ctrler.MessageInCtrler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebCtrlerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebCtrlerTest.class);

	private MockMvc mockMvc;

//	@InjectMocks
//	MessageInCtrler messageInCtrler;

	// @MockBean
	// private IBepsPackService bepsPackService;

	@Autowired
	private IRuleService ruleService;

	@Before
	public void setUp() {
//		MockitoAnnotations.openMocks(this);
//		this.mockMvc = MockMvcBuilders.standaloneSetup(messageInCtrler).build();
		mockMvc = MockMvcBuilders.standaloneSetup(new MessageInCtrler()).build();
		ruleService.execute();
	}

	@Test
	public void handleOutOk() throws Exception {
		String content = "";
		String url = "/message/handleOut";

		// when(bepsPackService.packBepsMessage()).thenReturn(1);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.request(HttpMethod.POST, url).contentType("application/json")
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())// 返回状态200为成功
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pig"))// 返回属性name为pig
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value(5))// 返回属性type需要为5
				.andDo(print()).andReturn();
		LOGGER.info(result.getResponse().getContentAsString());
	}
}