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
 * CCMS_990_bean
 */
public class CCMS_990_bean extends MessageBean{

	//报文头
	private com.giantstone.cnaps2.messagebean.recv.req.MsgHeader msgHeader = new com.giantstone.cnaps2.messagebean.recv.req.MsgHeader();

	public com.giantstone.cnaps2.messagebean.recv.req.MsgHeader getMsgHeader(){
		return msgHeader;
	}

	public void setMsgHeader(com.giantstone.cnaps2.messagebean.recv.req.MsgHeader msgHeader){
		this.msgHeader = msgHeader;
	}

	//报文体
	private com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_Out cCMS_990_Out = new com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_Out();

	public com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_Out getCCMS_990_Out(){
		return cCMS_990_Out;
	}

	public void setCCMS_990_Out(com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_Out cCMS_990_Out){
		this.cCMS_990_Out = cCMS_990_Out;
	}

	public Object getAttribute(String name){
		if("MsgHeader".equals(name)){
			return  this.getMsgHeader();
		}else if("CCMS_990_Out".equals(name)){
			return  this.getCCMS_990_Out();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("MsgHeader".equals(name)){
			this.setMsgHeader((com.giantstone.cnaps2.messagebean.recv.req.MsgHeader)value);
		}else if("CCMS_990_Out".equals(name)){
			this.setCCMS_990_Out((com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_Out)value);
		}
	}

	public void cover(MessageBean bean){
		CCMS_990_bean newBean = (CCMS_990_bean) bean;

		if( null != msgHeader&&null != newBean.getMsgHeader()){
			msgHeader.cover(newBean.getMsgHeader());
		}
		if( null != cCMS_990_Out&&null != newBean.getCCMS_990_Out()){
			cCMS_990_Out.cover(newBean.getCCMS_990_Out());
		}
	}

	public void validate(){
		//报文头正确性检查
		if ( null != msgHeader && !msgHeader.isNull() ){ 
			msgHeader.validate();
		}

		//报文体正确性检查
		if ( null != cCMS_990_Out && !cCMS_990_Out.isNull() ){ 
			cCMS_990_Out.validate();
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
		if( null != msgHeader){
			str = msgHeader.toString(true);
			if( null != str){
				buf.append("<a n=\"msgHeader\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != cCMS_990_Out){
			str = cCMS_990_Out.toString(true);
			if( null != str){
				buf.append("<a n=\"cCMS_990_Out\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.CCMS_990_bean\">");
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
		if( null != msgHeader){
			if (  !msgHeader.isNull() ){
				return false;
			}
		}
		if( null != cCMS_990_Out){
			if (  !cCMS_990_Out.isNull() ){
				return false;
			}
		}
		return true;
	}
}
