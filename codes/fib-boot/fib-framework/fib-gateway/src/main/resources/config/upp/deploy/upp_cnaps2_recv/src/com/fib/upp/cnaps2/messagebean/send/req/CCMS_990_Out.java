package com.fib.upp.cnaps2.messagebean.send.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * 通信级确认报文
 */
public class CCMS_990_Out extends MessageBean{

	//报文发起人
	private String origSender;

	public String getOrigSender(){
		return origSender;
	}

	public void setOrigSender(String origSender){
		this.origSender = origSender;
	}

	//报文发起日期
	private String origSendDate;

	public String getOrigSendDate(){
		return origSendDate;
	}

	public void setOrigSendDate(String origSendDate){
		this.origSendDate = origSendDate;
	}

	//报文类型代码
	private String mesgType2;

	public String getMesgType2(){
		return mesgType2;
	}

	public void setMesgType2(String mesgType2){
		this.mesgType2 = mesgType2;
	}

	// 通信级标识号
	private String messageIdentification;

	public String getMessageIdentification(){
		return messageIdentification;
	}

	public void setMessageIdentification(String messageIdentification){
		this.messageIdentification = messageIdentification;
	}

	//通信级参考号
	private String messageReferenceIdentification;

	public String getMessageReferenceIdentification(){
		return messageReferenceIdentification;
	}

	public void setMessageReferenceIdentification(String messageReferenceIdentification){
		this.messageReferenceIdentification = messageReferenceIdentification;
	}

	//处理状态
	private String messageProcessCode;

	public String getMessageProcessCode(){
		return messageProcessCode;
	}

	public void setMessageProcessCode(String messageProcessCode){
		this.messageProcessCode = messageProcessCode;
	}

	public Object getAttribute(String name){
		if("OrigSender".equals(name)){
			return  this.getOrigSender();
		}else if("OrigSendDate".equals(name)){
			return  this.getOrigSendDate();
		}else if("MesgType2".equals(name)){
			return  this.getMesgType2();
		}else if("MessageIdentification".equals(name)){
			return  this.getMessageIdentification();
		}else if("MessageReferenceIdentification".equals(name)){
			return  this.getMessageReferenceIdentification();
		}else if("MessageProcessCode".equals(name)){
			return  this.getMessageProcessCode();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("OrigSender".equals(name)){
			this.setOrigSender((String)value);
		}else if("OrigSendDate".equals(name)){
			this.setOrigSendDate((String)value);
		}else if("MesgType2".equals(name)){
			this.setMesgType2((String)value);
		}else if("MessageIdentification".equals(name)){
			this.setMessageIdentification((String)value);
		}else if("MessageReferenceIdentification".equals(name)){
			this.setMessageReferenceIdentification((String)value);
		}else if("MessageProcessCode".equals(name)){
			this.setMessageProcessCode((String)value);
		}
	}

	public void cover(MessageBean bean){
		CCMS_990_Out newBean = (CCMS_990_Out) bean;

		if( null != newBean.getOrigSender() && 0 != newBean.getOrigSender().length()){
			origSender = newBean.getOrigSender();
		}
		if( null != newBean.getOrigSendDate() && 0 != newBean.getOrigSendDate().length()){
			origSendDate = newBean.getOrigSendDate();
		}
		if( null != newBean.getMesgType2() && 0 != newBean.getMesgType2().length()){
			mesgType2 = newBean.getMesgType2();
		}
		if( null != newBean.getMessageIdentification() && 0 != newBean.getMessageIdentification().length()){
			messageIdentification = newBean.getMessageIdentification();
		}
		if( null != newBean.getMessageReferenceIdentification() && 0 != newBean.getMessageReferenceIdentification().length()){
			messageReferenceIdentification = newBean.getMessageReferenceIdentification();
		}
		if( null != newBean.getMessageProcessCode() && 0 != newBean.getMessageProcessCode().length()){
			messageProcessCode = newBean.getMessageProcessCode();
		}
	}

	public void validate(){
		//报文发起人非空检查
		if ( null == origSender){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"报文发起人(origSender)"}));
		}

			//报文发起人数据长度检查
			try{
			if ( origSender.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文发起人(origSender)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
			}

		//报文发起日期非空检查
		if ( null == origSendDate){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"报文发起日期(origSendDate)"}));
		}

			//报文发起日期数据长度检查
			try{
			if ( origSendDate.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文发起日期(origSendDate)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
			}

		//报文类型代码非空检查
		if ( null == mesgType2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"报文类型代码(mesgType2)"}));
		}

			//报文类型代码数据长度检查
			try{
			if ( mesgType2.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文类型代码(mesgType2)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
			}

		// 通信级标识号非空检查
		if ( null == messageIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{" 通信级标识号(messageIdentification)"}));
		}

			// 通信级标识号数据长度检查
			try{
			if ( messageIdentification.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{" 通信级标识号(messageIdentification)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
			}

		//通信级参考号非空检查
		if ( null == messageReferenceIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"通信级参考号(messageReferenceIdentification)"}));
		}

			//通信级参考号数据长度检查
			try{
			if ( messageReferenceIdentification.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"通信级参考号(messageReferenceIdentification)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
			}

		//处理状态非空检查
		if ( null == messageProcessCode){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"处理状态(messageProcessCode)"}));
		}

			//处理状态数据长度检查
			try{
			if ( messageProcessCode.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"处理状态(messageProcessCode)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"CCMS_990_Out", "UTF-8"}));
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
		if( null != origSender){
			buf.append("<a n=\"origSender\" t=\"str\">");
			buf.append(origSender);
			buf.append("</a>");
		}
		if( null != origSendDate){
			buf.append("<a n=\"origSendDate\" t=\"str\">");
			buf.append(origSendDate);
			buf.append("</a>");
		}
		if( null != mesgType2){
			buf.append("<a n=\"mesgType2\" t=\"str\">");
			buf.append(mesgType2);
			buf.append("</a>");
		}
		if( null != messageIdentification){
			buf.append("<a n=\"messageIdentification\" t=\"str\">");
			buf.append(messageIdentification);
			buf.append("</a>");
		}
		if( null != messageReferenceIdentification){
			buf.append("<a n=\"messageReferenceIdentification\" t=\"str\">");
			buf.append(messageReferenceIdentification);
			buf.append("</a>");
		}
		if( null != messageProcessCode){
			buf.append("<a n=\"messageProcessCode\" t=\"str\">");
			buf.append(messageProcessCode);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.send.req.CCMS_990_Out\">");
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
		if( null != origSender){
			return false;
		}
		if( null != origSendDate){
			return false;
		}
		if( null != mesgType2){
			return false;
		}
		if( null != messageIdentification){
			return false;
		}
		if( null != messageReferenceIdentification){
			return false;
		}
		if( null != messageProcessCode){
			return false;
		}
		return true;
	}
}
