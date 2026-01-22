package com.fib.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@Autowired
    private ProducerService producerService;

//	@Autowired
//	public TestController(RestTemplate restTemplate,LoadBalancerClient loadBalancer) {
//		this.restTemplate = restTemplate;
//		this.loadBalancer = loadBalancer;
//	}

	@GetMapping(value = "/echo/{str}")
	public String echo(@PathVariable String str) {
//		ServiceInstance serviceInstance = loadBalancer.choose("fib-order"); // 获取一个服务实例信息
//		URI serviceUri = null;
//		try {
//			serviceUri = new URI(serviceInstance.getUri() + "/echo/" + str);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // 构造完整的URL
//		return restTemplate.getForObject(serviceUri, String.class);
		String result =  producerService.callHello(str);
		return "Consumer received: " + result;
	}

}
