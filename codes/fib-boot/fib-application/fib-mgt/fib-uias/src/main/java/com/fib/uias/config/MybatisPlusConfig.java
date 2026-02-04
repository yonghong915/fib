package com.fib.uias.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@EnableTransactionManagement
@Configuration
@MapperScan({ "com.fib.uias.mapper", "com.fib.core.base.mapper" })
public class MybatisPlusConfig {
	@Bean
	MybatisPlusInterceptor paginationInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 阻止恶意或误操作的全表更新删除
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		// 分页插件
		PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
		pageInterceptor.setMaxLimit(500L);
		// 开启 count 的 join 优化,只针对部分 left join
		pageInterceptor.setOptimizeJoin(true);
		interceptor.addInnerInterceptor(pageInterceptor);
		return interceptor;
	}
}
