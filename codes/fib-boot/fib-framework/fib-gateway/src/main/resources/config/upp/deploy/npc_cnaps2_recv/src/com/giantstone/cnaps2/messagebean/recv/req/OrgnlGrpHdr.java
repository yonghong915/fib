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
 * 原报文主键组件
 */
public class OrgnlGrpHdr extends MessageBean{

	//原报文标识号
	private String originalMessageIdentification;

	public String getOriginalMessageIdentification(){
		return originalMessageIdentification;
	}

	public void setOriginalMessageIdentification(String originalMessageIdentification){
		this.originalMessageIdentification = originalMessageIdentification;
	}

	//原发起参与机构
	private String originalInstructingParty;

	public String getOriginalInstructingParty(){
		return originalInstructingParty;
	}

	public void setOriginalInstructingParty(String originalInstructingParty){
		this.originalInstructingParty = originalInstructingParty;
	}

	//原报文类型
	private String originalMessageType;

	public String getOriginalMessageType(){
		return originalMessageType;
	}

	public void setOriginalMessageType(String originalMessageType){
		this.originalMessageType = originalMessageType;
	}

	public Object getAttribute(String name){
		if("OriginalMessageIdentification".equals(name)){
			return  this.getOriginalMessageIdentification();
		}else if("OriginalInstructingParty".equals(name)){
			return  this.getOriginalInstructingParty();
		}else if("OriginalMessageType".equals(name)){
			return  this.getOriginalMessageType();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("OriginalMessageIdentification".equals(name)){
			this.setOriginalMessageIdentification((String)value);
		}else if("OriginalInstructingParty".equals(name)){
			this.setOriginalInstructingParty((String)value);
		}else if("OriginalMessageType".equals(name)){
			this.setOriginalMessageType((String)value);
		}
	}

	public void cover(MessageBean bean){
		OrgnlGrpHdr newBean = (OrgnlGrpHdr) bean;

		if( null != newBean.getOriginalMessageIdentification() && 0 != newBean.getOriginalMessageIdentification().length()){
			originalMessageIdentification = newBean.getOriginalMessageIdentification();
		}
		if( null != newBean.getOriginalInstructingParty() && 0 != newBean.getOriginalInstructingParty().length()){
			originalInstructingParty = newBean.getOriginalInstructingParty();
		}
		if( null != newBean.getOriginalMessageType() && 0 != newBean.getOriginalMessageType().length()){
			originalMessageType = newBean.getOriginalMessageType();
		}
	}

	public void validate(){
		//原报文标识号不为空则按以下规则校验
		if( null != originalMessageIdentification){
			//原报文标识号数据长度检查
			try{
			if ( originalMessageIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原报文标识号(originalMessageIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlGrpHdr", "UTF-8"}));
			}

		}

		//原发起参与机构不为空则按以下规则校验
		if( null != originalInstructingParty){
			//原发起参与机构数据长度检查
			try{
			if ( originalInstructingParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原发起参与机构(originalInstructingParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlGrpHdr", "UTF-8"}));
			}

		}

		//原报文类型不为空则按以下规则校验
		if( null != originalMessageType){
			//原报文类型数据长度检查
			try{
			if ( originalMessageType.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原报文类型(originalMessageType)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlGrpHdr", "UTF-8"}));
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
		if( null != originalMessageIdentification){
			buf.append("<a n=\"originalMessageIdentification\" t=\"str\">");
			buf.append(originalMessageIdentification);
			buf.append("</a>");
		}
		if( null != originalInstructingParty){
			buf.append("<a n=\"originalInstructingParty\" t=\"str\">");
			buf.append(originalInstructingParty);
			buf.append("</a>");
		}
		if( null != originalMessageType){
			buf.append("<a n=\"originalMessageType\" t=\"str\">");
			buf.append(originalMessageType);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr\">");
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
		if( null != originalMessageIdentification){
			return false;
		}
		if( null != originalInstructingParty){
			return false;
		}
		if( null != originalMessageType){
			return false;
		}
		return true;
	}
}
