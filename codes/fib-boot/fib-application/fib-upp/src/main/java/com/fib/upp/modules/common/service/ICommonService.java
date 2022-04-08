package com.fib.upp.modules.common.service;

import java.util.Map;


/**
 * 通用服务接口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-08 11:56:21
 */
public interface ICommonService {

	Map<String, Object> execute(Map<String, ? extends Object> context);
}
