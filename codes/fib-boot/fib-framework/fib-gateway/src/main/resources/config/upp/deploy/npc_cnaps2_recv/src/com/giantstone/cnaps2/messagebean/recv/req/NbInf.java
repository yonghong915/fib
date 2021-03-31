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
 * 期数信息
 */
public class NbInf extends MessageBean{

	//变更期数
	private String changeNumber;

	public String getChangeNumber(){
		return changeNumber;
	}

	public void setChangeNumber(String changeNumber){
		this.changeNumber = changeNumber;
	}

	//变更记录数目
	private String changeRecordNumber;

	public String getChangeRecordNumber(){
		return changeRecordNumber;
	}

	public void setChangeRecordNumber(String changeRecordNumber){
		this.changeRecordNumber = changeRecordNumber;
	}

	public Object getAttribute(String name){
		if("ChangeNumber".equals(name)){
			return  this.getChangeNumber();
		}else if("ChangeRecordNumber".equals(name)){
			return  this.getChangeRecordNumber();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("ChangeNumber".equals(name)){
			this.setChangeNumber((String)value);
		}else if("ChangeRecordNumber".equals(name)){
			this.setChangeRecordNumber((String)value);
		}
	}

	public void cover(MessageBean bean){
		NbInf newBean = (NbInf) bean;

		if( null != newBean.getChangeNumber() && 0 != newBean.getChangeNumber().length()){
			changeNumber = newBean.getChangeNumber();
		}
		if( null != newBean.getChangeRecordNumber() && 0 != newBean.getChangeRecordNumber().length()){
			changeRecordNumber = newBean.getChangeRecordNumber();
		}
	}

	public void validate(){
		//变更期数不为空则按以下规则校验
		if( null != changeNumber){
			//变更期数数据长度检查
			try{
			if ( changeNumber.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"变更期数(changeNumber)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NbInf", "UTF-8"}));
			}

		}

		//变更记录数目不为空则按以下规则校验
		if( null != changeRecordNumber){
			//变更记录数目数据长度检查
			try{
			if ( changeRecordNumber.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"变更记录数目(changeRecordNumber)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NbInf", "UTF-8"}));
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
		if( null != changeNumber){
			buf.append("<a n=\"changeNumber\" t=\"str\">");
			buf.append(changeNumber);
			buf.append("</a>");
		}
		if( null != changeRecordNumber){
			buf.append("<a n=\"changeRecordNumber\" t=\"str\">");
			buf.append(changeRecordNumber);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.NbInf\">");
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
		if( null != changeNumber){
			return false;
		}
		if( null != changeRecordNumber){
			return false;
		}
		return true;
	}
}
