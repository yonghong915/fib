package com.fib.upp.modules.epcc.service.impl;

import java.nio.charset.StandardCharsets;


import cn.hutool.core.util.HexUtil;

public class EPCCMsgRcvServlet {
	public static void main(String[] args) {
		String ss = "2002";
		System.out.println(HexUtil.encodeHexStr(ss, StandardCharsets.UTF_8));
		System.out.println(HexUtil.decodeHexStr("e696b9"));
		System.out.println(HexUtil.decodeHexStr("32",StandardCharsets.UTF_8));
	}
}
