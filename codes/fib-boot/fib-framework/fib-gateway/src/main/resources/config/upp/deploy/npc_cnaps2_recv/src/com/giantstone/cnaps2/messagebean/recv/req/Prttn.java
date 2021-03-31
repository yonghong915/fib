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
 * 报文分片信息组件
 */
public class Prttn extends MessageBean{

	//总记录数
	private String totalNumber;

	public String getTotalNumber(){
		return totalNumber;
	}

	public void setTotalNumber(String totalNumber){
		this.totalNumber = totalNumber;
	}

	//本报文记录起始序号
	private String startNumber;

	public String getStartNumber(){
		return startNumber;
	}

	public void setStartNumber(String startNumber){
		this.startNumber = startNumber;
	}

	//本报文记录截止序号
	private String endNumber;

	public String getEndNumber(){
		return endNumber;
	}

	public void setEndNumber(String endNumber){
		this.endNumber = endNumber;
	}

	public Object getAttribute(String name){
		if("TotalNumber".equals(name)){
			return  this.getTotalNumber();
		}else if("StartNumber".equals(name)){
			return  this.getStartNumber();
		}else if("EndNumber".equals(name)){
			return  this.getEndNumber();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("TotalNumber".equals(name)){
			this.setTotalNumber((String)value);
		}else if("StartNumber".equals(name)){
			this.setStartNumber((String)value);
		}else if("EndNumber".equals(name)){
			this.setEndNumber((String)value);
		}
	}

	public void cover(MessageBean bean){
		Prttn newBean = (Prttn) bean;

		if( null != newBean.getTotalNumber() && 0 != newBean.getTotalNumber().length()){
			totalNumber = newBean.getTotalNumber();
		}
		if( null != newBean.getStartNumber() && 0 != newBean.getStartNumber().length()){
			startNumber = newBean.getStartNumber();
		}
		if( null != newBean.getEndNumber() && 0 != newBean.getEndNumber().length()){
			endNumber = newBean.getEndNumber();
		}
	}

	public void validate(){
		//总记录数非空检查
		if ( null == totalNumber){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"总记录数(totalNumber)"}));
		}

			//总记录数数据长度检查
			try{
			if ( totalNumber.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"总记录数(totalNumber)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"Prttn", "UTF-8"}));
			}

		//本报文记录起始序号非空检查
		if ( null == startNumber){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"本报文记录起始序号(startNumber)"}));
		}

			//本报文记录起始序号数据长度检查
			try{
			if ( startNumber.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"本报文记录起始序号(startNumber)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"Prttn", "UTF-8"}));
			}

		//本报文记录截止序号非空检查
		if ( null == endNumber){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"本报文记录截止序号(endNumber)"}));
		}

			//本报文记录截止序号数据长度检查
			try{
			if ( endNumber.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"本报文记录截止序号(endNumber)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"Prttn", "UTF-8"}));
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
		if( null != totalNumber){
			buf.append("<a n=\"totalNumber\" t=\"str\">");
			buf.append(totalNumber);
			buf.append("</a>");
		}
		if( null != startNumber){
			buf.append("<a n=\"startNumber\" t=\"str\">");
			buf.append(startNumber);
			buf.append("</a>");
		}
		if( null != endNumber){
			buf.append("<a n=\"endNumber\" t=\"str\">");
			buf.append(endNumber);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.Prttn\">");
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
		if( null != totalNumber){
			return false;
		}
		if( null != startNumber){
			return false;
		}
		if( null != endNumber){
			return false;
		}
		return true;
	}
}
