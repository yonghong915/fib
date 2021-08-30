package com.giantstone.cnaps2.messagebean.send.req;

import com.fib.msgconverter.message.bean.*;

import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;

import java.util.*;

import com.giantstone.common.util.*;

/**
 * 批量包组头组件
 */
public class PKGGrpHdr extends MessageBean{

	public void validate(){
	String name = CodeUtil.Bytes2FormattedText(this.getClass().getName().getBytes());
	System.out.println("className="+this.getClass().getName()+"  name=" + name);
	}
}
