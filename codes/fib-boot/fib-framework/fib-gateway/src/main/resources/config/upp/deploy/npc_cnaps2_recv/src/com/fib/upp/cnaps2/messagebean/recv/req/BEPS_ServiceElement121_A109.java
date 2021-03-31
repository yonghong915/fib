package com.fib.upp.cnaps2.messagebean.recv.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * 委托收款划回
 */
public class BEPS_ServiceElement121_A109 extends MessageBean{

	//票据种类
	private String type;

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	//票据日期
	private String date;

	public String getDate(){
		return date;
	}

	public void setDate(String date){
		this.date = date;
	}

	//票据号码
	private String number;

	public String getNumber(){
		return number;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public Object getAttribute(String name){
		if("Type".equals(name)){
			return  this.getType();
		}else if("Date".equals(name)){
			return  this.getDate();
		}else if("Number".equals(name)){
			return  this.getNumber();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Type".equals(name)){
			this.setType((String)value);
		}else if("Date".equals(name)){
			this.setDate((String)value);
		}else if("Number".equals(name)){
			this.setNumber((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_ServiceElement121_A109 newBean = (BEPS_ServiceElement121_A109) bean;

		if( null != newBean.getType() && 0 != newBean.getType().length()){
			type = newBean.getType();
		}
		if( null != newBean.getDate() && 0 != newBean.getDate().length()){
			date = newBean.getDate();
		}
		if( null != newBean.getNumber() && 0 != newBean.getNumber().length()){
			number = newBean.getNumber();
		}
	}

	public void validate(){
		//票据种类非空检查
		if ( null == type){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据种类(type)"}));
		}

			//票据种类数据长度检查
			try{
			if ( type.getBytes("UTF-8").length> 2 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据种类(type)", "2"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A109", "UTF-8"}));
			}

		//票据种类指定值域检查
		if ( !"99".equals( type) && !"02".equals( type) && !"03".equals( type) && !"04".equals( type) ) {
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.value.in", new String[]{")票据种类(type)", "99/02/03/04"}));
		}

		//票据日期非空检查
		if ( null == date){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据日期(date)"}));
		}

			//票据日期数据长度检查
			try{
			if ( date.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据日期(date)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A109", "UTF-8"}));
			}

			//票据日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(date);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"票据日期(date)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		//票据号码非空检查
		if ( null == number){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据号码(number)"}));
		}

			//票据号码数据长度检查
			try{
			if ( number.getBytes("UTF-8").length> 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据号码(number)", "32"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A109", "UTF-8"}));
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
		if( null != type){
			buf.append("<a n=\"type\" t=\"str\">");
			buf.append(type);
			buf.append("</a>");
		}
		if( null != date){
			buf.append("<a n=\"date\" t=\"datetime\">");
			buf.append(date);
			buf.append("</a>");
		}
		if( null != number){
			buf.append("<a n=\"number\" t=\"str\">");
			buf.append(number);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.BEPS_ServiceElement121_A109\">");
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
		if( null != type){
			return false;
		}
		if( null != date){
			return false;
		}
		if( null != number){
			return false;
		}
		return true;
	}
}
