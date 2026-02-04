package com.fib.pcms.config;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DefaultQuartzJobBean extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(DefaultQuartzJobBean.class);

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		// 拿到Spring的上下文，可以自由的做业务处理
		try {
			jobExecutionContext.getScheduler().getContext().get("applicationContext");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		TriggerKey key = jobExecutionContext.getTrigger().getKey();
		String group = key.getGroup();
		String name = key.getName();
		// 业务处理 ...

		logger.info(" 执行中 ... " + group + "\t" + name);
	}

}
