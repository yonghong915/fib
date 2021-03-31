package com.giantstone.cnaps2.messagebean.recv.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * 附加域list
 */
public class HVPS_111_OutHvps extends MessageBean{

	//list
	private String ustrd;

	public String getUstrd(){
		return ustrd;
	}

	public void setUstrd(String ustrd){
		this.ustrd = ustrd;
	}

	public Object getAttribute(String name){
		if("Ustrd".equals(name)){
			return  this.getUstrd();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Ustrd".equals(name)){
			this.setUstrd((String)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_111_OutHvps newBean = (HVPS_111_OutHvps) bean;

		if( null != newBean.getUstrd() && 0 != newBean.getUstrd().length()){
			ustrd = newBean.getUstrd();
		}
	}

	public void validate(){
		//list不为空则按以下规则校验
		if( null != ustrd){
			//list数据长度检查
			if ( ustrd.getBytes().length > 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"list(ustrd)", "35"}));
			}

		}


	}

	public String toString(){
		return toString(false);
	}

	public String toString(boolean isWrap){
		return toString(isWrap,false);
	}

	public String toString(boolean isWrap,boolean isTable){
		StringBuffer buf = new StringBuffer(10240);
		StringBuffer tableBuf = new StringBuffer(2048);
		String str = null;
		if( null != ustrd){
			buf.append("<a n=\"ustrd\" t=\"str\">");
			buf.append(ustrd);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps\">");
			}
			buf.append("</b>");
			if( !isWrap ){
				buf = new StringBuffer(buf.toString());
				buf.insert(0,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				return buf.toString();
			}else{
				return buf.toString();
			}
		}
	}
	public boolean isNull(){
		if( null != ustrd){
			return false;
		}
		return true;
	}
}
