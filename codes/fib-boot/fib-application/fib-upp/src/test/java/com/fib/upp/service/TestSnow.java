package com.fib.upp.service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import com.fib.commons.util.CommUtils;
import com.fib.upp.entity.BizTbClobEntity;
import com.fib.upp.entity.CommunicationEventEntity;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;

public class TestSnow {

	public static void main(String[] args) {
		String co = FileUtil.readUtf8String("data/data.txt");
		// String co =
		// "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa天dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
		System.out.println("sourceData size:" + co.getBytes().length);
		try {
//			byte[] rts = ZipUtil.gzip(co.getBytes("UTF-8"));
//			System.out.println("gzip data size:" + rts.length);
//			String ret = cn.hutool.core.codec.Base64.encode(rts);
//			System.out.println("rt=" + ret.length());
//
//			String cont = Base64.getEncoder().encodeToString(rts);
//			System.out.println("base64 data size:" + cont.getBytes().length);
//			System.out.println(cont);
//			byte[] decHex = HexUtil.decodeHex(hexStr);
//			System.out.println("decHex=" + decHex.length);
//
//			String ss = ZipUtil.unGzip(decHex, "UTF-8");
			// System.out.println(ss.length());

			String baseSrc = CommUtils.gzip(co, "UTF-8");
			System.out.println("baseStr = " + baseSrc);
			System.out.println(baseSrc.length());

			String src = CommUtils.unzip(baseSrc, "UTF-8");
			System.out.println("src=" + src.length());
			// System.out.println(src);
		} catch (UtilException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
