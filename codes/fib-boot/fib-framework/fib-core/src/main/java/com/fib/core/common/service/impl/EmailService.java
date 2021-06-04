package com.fib.core.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fib.core.common.entity.Email;
import com.fib.core.common.service.IAsyncService;

/**
 * 邮件服务
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Service("emailService")
public class EmailService implements IAsyncService {
	private Logger logger = LoggerFactory.getLogger(EmailService.class);

	public void sendEmail(Email email) {
		String filePath = "F:\\logs\\" + UUID.randomUUID().toString();
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void execute(Object source) {
		logger.info("source={}", source);
		this.sendEmail(null);
	}
}
