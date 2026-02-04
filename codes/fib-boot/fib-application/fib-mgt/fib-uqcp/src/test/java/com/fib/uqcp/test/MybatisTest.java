package com.fib.uqcp.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fib.uqcp.UqcpApplication;
import com.fib.uqcp.entity.ChineseMedicine;
import com.fib.uqcp.mapper.ChineseMedicineMapper;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

@SpringBootTest(classes = UqcpApplication.class)
public class MybatisTest {
	@Autowired
	private ChineseMedicineMapper chineseMedicineMapper;
	@Test
	void query() {
		//ChineseMedicineService bean = SpringUtil.getBean(ChineseMedicineService.class);
		ChineseMedicine chineseMedicine = new ChineseMedicine();
		Snowflake snowflake = IdUtil.getSnowflake(0, 0);
		long value = snowflake.nextId();
		chineseMedicine.setId(value);
		//MetaObjectHandler s;WhereSqlNode
		 
		chineseMedicineMapper.insert(chineseMedicine);
	}
}
