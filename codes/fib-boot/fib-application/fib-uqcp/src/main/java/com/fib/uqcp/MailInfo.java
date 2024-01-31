package com.fib.uqcp;

import java.util.Date;

public record MailInfo(String host, String username, String password, Boolean validate, String fromAddress, String[] sendAddress, String[] ccAddress,
		String subject, Date sentDate, String content, String[] filePath) {


}
