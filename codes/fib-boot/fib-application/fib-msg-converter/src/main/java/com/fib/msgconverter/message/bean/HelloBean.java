package com.fib.msgconverter.message.bean;

import com.giantstone.common.util.CodeUtil;

public class HelloBean extends MessageBean {

	@Override
	public void validate() {
		System.out.println("HelloBean.....");
		byte[] byts = CodeUtil.HextoByte("Hello");
		System.out.println(byts.length);
		System.out.println("HelloBean3.....");
	}

}
