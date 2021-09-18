package com.fib.msgconverter;

import com.fib.msgconverter.commgateway.util.EnumConstants;

public class EnumTest {
	public static void main(String[] args) {
		int code = EnumConstants.MessageObjectType.JSON.getCode();
		String name = EnumConstants.MessageObjectType.JSON.getName();
		System.out.println("code=" + code + " name=" + name);
		System.out.println(EnumConstants.ProcessorType.getCodeByName("SAVE_AND_TRANSFORM"));
		System.out.println(EnumConstants.ProcessorType.getNameByCode(149));
	}
}
